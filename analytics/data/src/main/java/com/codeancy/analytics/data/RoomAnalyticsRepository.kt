package com.codeancy.analytics.data

import com.codeancy.analytics.domain.AnalyticsRepository
import com.codeancy.analytics.domain.AnalyticsValue
import com.codeancy.core.database.dao.AnalyticsDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.milliseconds

class RoomAnalyticsRepository(
    private val analyticsDao: AnalyticsDao
) : AnalyticsRepository {
    override suspend fun getAnalyticsValues(): AnalyticsValue {

        return withContext(Dispatchers.IO) {
            val totalDistanceRun = async { analyticsDao.getTotalDistance() }
            val totalTimeRun = async { analyticsDao.getTotalTimeRun() }
            val avgDistancePerRun = async { analyticsDao.avgDistancePerRun() }
            val avgPace = async { analyticsDao.getAveragePacePerRun() }
            val fastestEverRun = async { analyticsDao.getMaxRunSpeed() }
            AnalyticsValue(
                totalDistanceRun = totalDistanceRun.await(),
                totalTimeRun = totalTimeRun.await().milliseconds,
                avgDistancePerRun = avgDistancePerRun.await(),
                avgPace = avgPace.await(),
                fastestEverRun = fastestEverRun.await()
            )
        }
    }
}