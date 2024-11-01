package com.codeancy.runique

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navDeepLink
import com.codeancy.auth.presentation.intro.IntroScreenRoot
import com.codeancy.auth.presentation.login.LoginScreenRoot
import com.codeancy.auth.presentation.register.RegisterScreenRoot
import com.codeancy.run.presentation.active_run.ActiveRunScreenRoot
import com.codeancy.run.presentation.active_run.service.ActiveRunService
import com.codeancy.run.presentation.run_overview.RunOverviewScreenRoot

@Composable
fun NavigationRoot(
    navController: NavHostController,
    isLoggedIn: Boolean,
    onAnalyticsClick: () -> Unit
) {

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) "run" else "auth"
    ) {
        authGraph(navController)
        runGraph(navController, onAnalyticsClick)
    }

}

private fun NavGraphBuilder.authGraph(navController: NavHostController) {

    navigation(startDestination = "intro", route = "auth") {

        composable("intro") {
            IntroScreenRoot(
                onSignUpClick = {
                    navController.navigate("register") {
                        launchSingleTop = true
                    }
                },
                onSignInClick = {
                    navController.navigate("login") {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable("register") {
            RegisterScreenRoot(
                onSignInClick = {
                    navController.navigate("login") {
                        popUpTo("register") {
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true
                    }
                },
                onSuccessfulRegistration = {
                    navController.navigate("login")
                }
            )
        }

        composable("login") {
            LoginScreenRoot(
                onLoginSuccess = {
                    navController.navigate("run") {
                        popUpTo("auth") {
                            inclusive = true
                        }
                    }
                },
                onSignUpClick = {
                    navController.navigate("register") {
                        popUpTo("login") {
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true
                    }
                }
            )
        }

    }

}

private fun NavGraphBuilder.runGraph(
    navController: NavHostController,
    onAnalyticsClick: () -> Unit
) {
    navigation("run_overview", route = "run") {
        composable("run_overview") {
            RunOverviewScreenRoot(
                onLogoutClick = {
                    navController.navigate("auth") {
                        popUpTo("run") {
                            inclusive = true
                        }
                    }
                },
                onRunStartClick = {
                    navController.navigate("active_run")
                },
                onAnalyticsClick = onAnalyticsClick
            )
        }
        composable(
            "active_run",
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "runique://active_run"
                }
            )
        ) {

            val context = LocalContext.current
            ActiveRunScreenRoot(
                onServiceToggle = { shouldServiceRun ->
                    if (shouldServiceRun) {
                        context.startService(
                            ActiveRunService.createStartIntent(
                                context,
                                MainActivity::class.java
                            )
                        )
                    } else {
                        context.startService(
                            ActiveRunService.createStopIntent(context)
                        )
                    }
                },
                onBack = {
                    navController.navigateUp()
                },
                onFinish = {
                    navController.navigateUp()
                }
            )

        }
    }
}