package com.codeancy.runique

import android.app.Application
import android.content.Context
import com.codeancy.auth.data.di.authDataModule
import com.codeancy.auth.presentation.di.authViewModelModule
import com.codeancy.core.data.di.coreDataModule
import com.codeancy.core.database.di.databaseModule
import com.codeancy.run.data.di.runDataModule
import com.codeancy.run.location.di.locationModule
import com.codeancy.run.network.di.networkModule
import com.codeancy.run.presentation.di.runPresentationModule
import com.codeancy.runique.di.appModule
import com.google.android.play.core.splitcompat.SplitCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin
import timber.log.Timber

class RuniqueApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        startKoin {
            androidLogger()
            androidContext(this@RuniqueApplication)
            workManagerFactory()
            modules(
                authDataModule,
                authViewModelModule,
                appModule,
                coreDataModule,
                runPresentationModule,
                locationModule,
                databaseModule,
                networkModule,
                runDataModule
            )
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        SplitCompat.install(this)
    }

}