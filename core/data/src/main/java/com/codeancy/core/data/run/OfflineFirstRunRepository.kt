package com.codeancy.core.data.run

import com.codeancy.core.data.networking.get
import com.codeancy.core.database.dao.RunPendingSyncDao
import com.codeancy.core.database.mappers.toRun
import com.codeancy.core.domain.SessionStorage
import com.codeancy.core.domain.run.LocalRunDataSource
import com.codeancy.core.domain.run.RemoteRunDataSource
import com.codeancy.core.domain.run.Run
import com.codeancy.core.domain.run.RunId
import com.codeancy.core.domain.run.RunRepository
import com.codeancy.core.domain.run.SyncRunScheduler
import com.codeancy.core.domain.util.DataError
import com.codeancy.core.domain.util.EmptyResult
import com.codeancy.core.domain.util.Result
import com.codeancy.core.domain.util.asEmptyDataResult
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.plugin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OfflineFirstRunRepository(
    private val localRunDataSource: LocalRunDataSource,
    private val remoteRunDataSource: RemoteRunDataSource,
    private val applicationScope: CoroutineScope,
    private val runPendingSyncDao: RunPendingSyncDao,
    private val sessionStorage: SessionStorage,
    private val syncRunScheduler: SyncRunScheduler,
    private val client: HttpClient
) : RunRepository {

    override fun getRuns(): Flow<List<Run>> {
        return localRunDataSource.getRuns()
    }

    override suspend fun fetchRuns(): EmptyResult<DataError> {
        return when (val result = remoteRunDataSource.getRuns()) {
            is Result.Success -> {
                applicationScope.async {
                    localRunDataSource.upsertRuns(result.data).asEmptyDataResult()
                }.await()
            }

            is Result.Error -> {
                result.asEmptyDataResult()
            }
        }
    }

    override suspend fun upsertRun(run: Run, mapPicture: ByteArray): EmptyResult<DataError> {
        val localResult = localRunDataSource.upsertRun(run)
        if (localResult !is Result.Success) {
            return localResult.asEmptyDataResult()
        }
        val runWithId = run.copy(id = localResult.data)
        return when (val remoteResult = remoteRunDataSource.postRun(runWithId, mapPicture)) {
            is Result.Error -> {
                applicationScope.launch {
                    syncRunScheduler.scheduleSync(
                        SyncRunScheduler.SyncType.CreateRun(
                            runWithId,
                            mapPictureBytes = mapPicture
                        )
                    )
                }.join()
                Result.Success(Unit)
            }

            is Result.Success -> {
                applicationScope.async {
                    localRunDataSource.upsertRun(remoteResult.data)
                }.await()
                remoteResult.asEmptyDataResult()
            }
        }
    }

    override suspend fun deleteRun(id: RunId) {
        localRunDataSource.deleteRun(id)

        val isPendingSync = runPendingSyncDao.getRunPendingSyncEntity(runId = id) != null

        if (isPendingSync) {
            runPendingSyncDao.deleteRunPendingEntity(id)
        } else {
            val remoteResult = applicationScope.async {
                remoteRunDataSource.deleteRun(id)
            }.await()

            if (remoteResult is Result.Error) {
                applicationScope.launch {
                    syncRunScheduler.scheduleSync(SyncRunScheduler.SyncType.DeleteRun(runId = id))
                }.join()
            }
        }
    }


    override suspend fun syncPendingRuns() {
        withContext(Dispatchers.IO) {
            val userId = sessionStorage.get()?.userId ?: return@withContext

            val createdRuns = async {
                runPendingSyncDao.getAllRunPendingSyncEntities(userId)
            }
            val deletedRuns = async {
                runPendingSyncDao.getAllDeletedRunSyncEntities(userId)
            }

            val createdJobs = createdRuns.await().map {
                launch {
                    val run = it.run.toRun()
                    when (remoteRunDataSource.postRun(run, it.mapPictureBytes)) {
                        is Result.Error -> Unit
                        is Result.Success -> {
                            applicationScope.launch {
                                runPendingSyncDao.deleteRunPendingEntity(it.runId)
                            }.join()
                        }
                    }
                }
            }

            val deleteJobs = deletedRuns.await().map {
                launch {
                    when (remoteRunDataSource.deleteRun(it.runId)) {
                        is Result.Error -> Unit
                        is Result.Success -> {
                            applicationScope.launch {
                                runPendingSyncDao.deleteDeletedRunSyncEntity(it.runId)
                            }.join()
                        }
                    }
                }
            }

            createdJobs.forEach { it.join() }
            deleteJobs.forEach { it.join() }

        }
    }

    override suspend fun deleteAllRuns() {
        localRunDataSource.deleteAllRuns()
    }

    override suspend fun logout(): EmptyResult<DataError.Network> {
        val result = client.get<Unit>(
            route = "/logout",
        ).asEmptyDataResult()

        client.plugin(Auth).providers.filterIsInstance<BearerAuthProvider>()
            .firstOrNull()
            ?.clearToken()

        return result
    }

}