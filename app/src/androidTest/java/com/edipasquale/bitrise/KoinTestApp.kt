package com.edipasquale.bitrise

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.koinApplication

class KoinTestApp : Application() {

    private lateinit var koin: KoinApplication

    override fun onCreate() {
        super.onCreate()

        koin = startKoin {
            androidContext(this@KoinTestApp)
        }
    }

    fun setUpModule(module: Module) {
        koin.modules(module).createEagerInstances()
    }

    fun closeKoin() {
        koin.close()
    }
}