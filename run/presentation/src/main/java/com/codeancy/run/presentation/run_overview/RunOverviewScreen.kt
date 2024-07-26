package com.codeancy.run.presentation.run_overview

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codeancy.core.presentation.design_system.AnalyticsIcon
import com.codeancy.core.presentation.design_system.LogoIcon
import com.codeancy.core.presentation.design_system.LogoutIcon
import com.codeancy.core.presentation.design_system.RunIcon
import com.codeancy.core.presentation.design_system.RuniqueTheme
import com.codeancy.core.presentation.design_system.components.RuniqueFloatingActionButton
import com.codeancy.core.presentation.design_system.components.RuniqueScaffold
import com.codeancy.core.presentation.design_system.components.RuniqueToolbar
import com.codeancy.core.presentation.design_system.components.util.DropDownItem
import com.codeancy.run.presentation.R
import com.codeancy.run.presentation.run_overview.components.RunListItem
import org.koin.androidx.compose.koinViewModel


@Composable
fun RunOverviewScreenRoot(
    viewModel: RunOverviewViewModel = koinViewModel(),
    onLogoutClick: () -> Unit,
    onRunStartClick: () -> Unit,
    onAnalyticsClick: () -> Unit
) {

    RunOverviewScreen(
        state = viewModel.state
    ) { action ->
        when (action) {
            RunOverviewAction.OnStartClick -> {
                onRunStartClick()
            }

            RunOverviewAction.OnLogoutClick -> {
                onLogoutClick()
            }

            RunOverviewAction.OnAnalyticsClick -> {
                onAnalyticsClick()
            }

            else -> Unit
        }
        viewModel.onAction(action)
    }

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun RunOverviewScreen(
    state: RunOverviewState,
    onAction: (RunOverviewAction) -> Unit
) {

    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = topAppBarState
    )

    RuniqueScaffold(
        topAppBar = {
            RuniqueToolbar(
                showBackButton = false,
                title = stringResource(id = R.string.runique),
                scrollBehavior = scrollBehavior,
                startContent = {
                    Icon(
                        imageVector = LogoIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(30.dp)
                    )
                },
                menu = listOf(
                    DropDownItem(
                        title = stringResource(id = R.string.analytics),
                        icon = AnalyticsIcon
                    ),
                    DropDownItem(title = stringResource(id = R.string.logout), icon = LogoutIcon)
                ),
                onMenuItemClick = { index ->
                    when (index) {
                        0 -> {
                            onAction(RunOverviewAction.OnAnalyticsClick)
                        }

                        1 -> {
                            onAction(RunOverviewAction.OnLogoutClick)
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            RuniqueFloatingActionButton(
                icon = RunIcon,
                onClick = { onAction(RunOverviewAction.OnStartClick) }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
//                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .padding(horizontal = 16.dp),
            contentPadding = padding,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            items(state.runs, key = { it.id }) { runUi ->

                RunListItem(
                    runUi = runUi,
                    modifier = Modifier
                        .animateItemPlacement()
                ) {
                    onAction(RunOverviewAction.OnDeleteRun(runUi))
                }

            }

        }

    }

}

@Preview
@Composable
private fun RunOverviewScreenPreview() {
    RuniqueTheme {
        RunOverviewScreen(state = RunOverviewState()) {

        }
    }
}