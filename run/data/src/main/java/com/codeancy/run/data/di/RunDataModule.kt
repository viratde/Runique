package com.codeancy.run.data.di

import com.codeancy.core.domain.run.SyncRunScheduler
import com.codeancy.run.data.CreateRunWorker
import com.codeancy.run.data.DeleteRunWorker
import com.codeancy.run.data.FetchRunsWorker
import com.codeancy.run.data.SyncWorkScheduler
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val runDataModule = module {

    workerOf(::FetchRunsWorker)

    workerOf(::DeleteRunWorker)

    workerOf(::CreateRunWorker)

    singleOf(::SyncWorkScheduler).bind<SyncRunScheduler>()
}