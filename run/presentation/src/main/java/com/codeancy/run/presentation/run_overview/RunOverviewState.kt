package com.codeancy.run.presentation.run_overview

import com.codeancy.run.presentation.run_overview.model.RunUi

data class RunOverviewState(
    val runs: List<RunUi> = listOf()
)