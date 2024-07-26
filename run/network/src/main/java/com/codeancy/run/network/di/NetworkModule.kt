package com.codeancy.run.network.di

import com.codeancy.core.domain.run.RemoteRunDataSource
import com.codeancy.run.network.KtorRemoteRunDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val networkModule = module {
    singleOf(::KtorRemoteRunDataSource).bind<RemoteRunDataSource>()
}