package com.codeancy.core.database.dao

import androidx.room.Dao
import androidx.room.Query


@Dao
interface AnalyticsDao {

    @Query("SELECT SUM(distanceMeters) FROM RunEntity")
    suspend fun getTotalDistance(): Int

    @Query("SELECT SUM(durationMillis) FROM RunEntity")
    suspend fun getTotalTimeRun(): Long

    @Query("SELECT MAX(maxSpeedKmh) FROM RunEntity")
    suspend fun getMaxRunSpeed(): Double

    @Query("SELECT AVG(distanceMeters) FROM RunEntity")
    suspend fun avgDistancePerRun(): Double

    @Query("SELECT AVG((durationMillis / 60000.0) / (distanceMeters / 1000)) FROM RunEntity")
    suspend fun getAveragePacePerRun(): Double
}