package com.codeancy.run.location.di

import com.codeancy.run.domain.LocationObserver
import com.codeancy.run.location.AndroidLocationObserver
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val locationModule = module {

    singleOf(::AndroidLocationObserver).bind<LocationObserver>()

}