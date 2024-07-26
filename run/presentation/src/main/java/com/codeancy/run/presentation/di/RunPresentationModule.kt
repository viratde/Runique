package com.codeancy.run.presentation.di

import com.codeancy.run.domain.RunningTracker
import com.codeancy.run.presentation.active_run.ActiveRunViewModel
import com.codeancy.run.presentation.run_overview.RunOverviewViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val runPresentationModule = module {

    single<RunningTracker> {
        RunningTracker(
            get(),
            get()
        )
    }

    viewModelOf(::RunOverviewViewModel)
    viewModelOf(::ActiveRunViewModel)
}