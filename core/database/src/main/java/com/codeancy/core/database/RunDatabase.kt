package com.codeancy.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.codeancy.core.database.dao.AnalyticsDao
import com.codeancy.core.database.dao.RunDao
import com.codeancy.core.database.dao.RunPendingSyncDao
import com.codeancy.core.database.model.DeletedRunSyncEntity
import com.codeancy.core.database.model.RunEntity
import com.codeancy.core.database.model.RunPendingSyncEntity


@Database(
    entities = [RunEntity::class, RunPendingSyncEntity::class, DeletedRunSyncEntity::class],
    version = 2
)
abstract class RunDatabase : RoomDatabase() {
    abstract val runDao: RunDao
    abstract val runPendingSyncDao: RunPendingSyncDao
    abstract val analyticsDao: AnalyticsDao
}