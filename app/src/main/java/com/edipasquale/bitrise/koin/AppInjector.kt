package com.edipasquale.bitrise.koin

import android.content.Context
import com.edipasquale.bitrise.repository.AuthRepository
import com.edipasquale.bitrise.repository.SimpleAuthRepository
import com.edipasquale.bitrise.source.AuthSource
import com.edipasquale.bitrise.source.SimpleAuthSource
import com.edipasquale.bitrise.validator.FieldValidator
import com.edipasquale.bitrise.validator.SimpleFieldValidator
import com.edipasquale.bitrise.viewmodel.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class AppInjector {

    val appModule = module {

        viewModel {
            MainViewModel(get(), get())
        }

        single {
            SimpleFieldValidator() as FieldValidator
        }

        single {
            SimpleAuthSource() as AuthSource
        }

        single {
            SimpleAuthRepository(get()) as AuthRepository
        }
    }

    fun setup(context: Context) = startKoin {
            androidContext(context)
            androidLogger()
            modules(appModule)
        }
}