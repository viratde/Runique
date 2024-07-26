package com.codeancy.analytics.data.di

import com.codeancy.analytics.data.RoomAnalyticsRepository
import com.codeancy.analytics.domain.AnalyticsRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


val analyticsDataModule = module {
    singleOf(::RoomAnalyticsRepository).bind<AnalyticsRepository>()
}