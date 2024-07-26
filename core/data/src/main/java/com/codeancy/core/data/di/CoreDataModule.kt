package com.codeancy.core.data.di

import com.codeancy.core.data.auth.EncryptedSessionStorage
import com.codeancy.core.data.networking.HttpClientFactory
import com.codeancy.core.data.run.OfflineFirstRunRepository
import com.codeancy.core.domain.SessionStorage
import com.codeancy.core.domain.run.RunRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreDataModule = module {


    single {
        HttpClientFactory(get()).build()
    }

    singleOf(::EncryptedSessionStorage).bind<SessionStorage>()

    singleOf(::OfflineFirstRunRepository).bind<RunRepository>()
}