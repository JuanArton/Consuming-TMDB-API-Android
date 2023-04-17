package com.juanarton

import android.app.Application
import com.juanarton.core.di.repositoryModule
import com.juanarton.moviecatalog.di.useCaseModule
import com.juanarton.moviecatalog.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyApplication)
            modules(
                listOf(
                    useCaseModule,
                    repositoryModule,
                    viewModelModule
                )
            )
        }
    }
}