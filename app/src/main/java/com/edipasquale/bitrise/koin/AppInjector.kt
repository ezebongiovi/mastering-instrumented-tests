package com.edipasquale.bitrise.koin

import android.content.Context
import com.edipasquale.bitrise.source.SimpleAuthSource
import com.edipasquale.bitrise.validator.SimpleFieldValidator
import com.edipasquale.bitrise.viewmodel.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module

class AppInjector {

    private val appModule = module {

        single {
            SimpleFieldValidator()
        }

        single {
            SimpleAuthSource()
        }

        single {
            MainViewModel(get(), get())
        }
    }

    fun setup(context: Context) {
        startKoin {
            androidContext(context)
            androidLogger()
            modules(appModule)
        }
    }
}