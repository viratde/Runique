package com.codeancy.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.codeancy.core.database.model.DeletedRunSyncEntity
import com.codeancy.core.database.model.RunPendingSyncEntity


@Dao
interface RunPendingSyncDao {

    @Query("SELECT * FROM runpendingsyncentity WHERE userId=:userId")
    suspend fun getAllRunPendingSyncEntities(userId: String): List<RunPendingSyncEntity>


    @Query("SELECT * FROM runpendingsyncentity WHERE runId=:runId")
    suspend fun getRunPendingSyncEntity(runId: String): RunPendingSyncEntity?

    @Upsert
    suspend fun upsertRunPendingEntity(runPendingSyncEntity: RunPendingSyncEntity)

    @Query("DELETE FROM runpendingsyncentity WHERE runId=:runId")
    suspend fun deleteRunPendingEntity(runId: String)


    @Query("SELECT * FROM deletedrunsyncentity WHERE userId=:userId")
    suspend fun getAllDeletedRunSyncEntities(userId: String): List<DeletedRunSyncEntity>

    @Upsert
    suspend fun upsertDeletedRunSyncEntity(deletedRunSyncEntity: DeletedRunSyncEntity)

    @Query("DELETE FROM deletedrunsyncentity WHERE runId=:runId")
    suspend fun deleteDeletedRunSyncEntity(runId:String)

}