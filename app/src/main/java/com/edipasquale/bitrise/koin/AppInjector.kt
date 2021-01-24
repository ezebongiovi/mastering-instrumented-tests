package com.edipasquale.bitrise.koin

import android.app.Application
import com.edipasquale.bitrise.repository.AuthRepository
import com.edipasquale.bitrise.repository.SimpleAuthRepository
import com.edipasquale.bitrise.source.auth.AuthSource
import com.edipasquale.bitrise.source.auth.FirebaseAuthSource
import com.edipasquale.bitrise.source.session.SessionManager
import com.edipasquale.bitrise.validator.FieldValidator
import com.edipasquale.bitrise.validator.SimpleFieldValidator
import com.edipasquale.bitrise.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class AppInjector {

    val appModule = module {

        viewModel {
            AuthViewModel(get(), get())
        }

        single<FieldValidator> {
            SimpleFieldValidator()
        }

        single<AuthSource> {
            FirebaseAuthSource()
        }

        single<AuthRepository> {
            SimpleAuthRepository(get())
        }

        single<SessionManager> { SessionManager(androidContext()) }

        factory { FirebaseAuth.getInstance() }
    }

    fun setup(applicationContext: Application) = startKoin {
        androidContext(applicationContext)
        modules(appModule)
    }
}