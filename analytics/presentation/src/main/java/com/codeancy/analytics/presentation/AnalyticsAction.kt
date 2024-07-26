package com.codeancy.analytics.presentation

sealed interface AnalyticsAction {
    data object OnBackClick : AnalyticsAction
}