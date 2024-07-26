package com.codeancy.runique.di

import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.codeancy.runique.MainViewModel
import com.codeancy.runique.RuniqueApplication
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {

    single<SharedPreferences> {

        EncryptedSharedPreferences(
            androidApplication(),
            "authPref",
            MasterKey(androidApplication()),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    }

    viewModelOf(::MainViewModel)

    single<CoroutineScope> {
        (androidApplication() as RuniqueApplication).applicationScope
    }
}