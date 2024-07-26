package com.codeancy.auth.data.di

import com.codeancy.auth.data.AuthRepositoryImpl
import com.codeancy.auth.data.EmailPatternValidator
import com.codeancy.auth.domain.AuthRepository
import com.codeancy.auth.domain.PatternValidator
import com.codeancy.auth.domain.UserDataValidator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authDataModule = module {

    single<PatternValidator> {
        EmailPatternValidator
    }

    singleOf(::UserDataValidator)

    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()

}