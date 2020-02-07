package com.edipasquale.bitrise

import android.app.Application
import com.edipasquale.bitrise.koin.AppInjector

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        AppInjector().setup(this)
    }
}