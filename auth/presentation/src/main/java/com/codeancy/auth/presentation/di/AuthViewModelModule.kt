package com.codeancy.auth.presentation.di

import com.codeancy.auth.presentation.login.LoginViewModel
import com.codeancy.auth.presentation.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val authViewModelModule = module {
    viewModelOf(::RegisterViewModel)
    viewModelOf(::LoginViewModel)
}