package com.codeancy.core.database.di

import androidx.room.Room
import com.codeancy.core.database.RoomLocalRunRunDataSource
import com.codeancy.core.database.RunDatabase
import com.codeancy.core.domain.run.LocalRunDataSource
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val databaseModule = module {

    single {
        Room.databaseBuilder(
            androidApplication(),
            RunDatabase::class.java,
            "run.db"
        )
            .fallbackToDestructiveMigration().build()
    }

    single {
        get<RunDatabase>().runDao
    }

    single {
        get<RunDatabase>().runPendingSyncDao
    }

    singleOf(::RoomLocalRunRunDataSource).bind<LocalRunDataSource>()

}