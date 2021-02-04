package com.edipasquale.bitrise.koin

import android.app.Application
import androidx.room.Room
import com.edipasquale.bitrise.BuildConfig
import com.edipasquale.bitrise.db.AppDatabase
import com.edipasquale.bitrise.repository.auth.AuthRepository
import com.edipasquale.bitrise.repository.auth.SimpleAuthRepository
import com.edipasquale.bitrise.repository.main.APIMainRepository
import com.edipasquale.bitrise.repository.main.MainRepository
import com.edipasquale.bitrise.source.auth.AuthSource
import com.edipasquale.bitrise.source.auth.FirebaseAuthSource
import com.edipasquale.bitrise.source.main.MainLocalSource
import com.edipasquale.bitrise.source.main.MainNetworkSource
import com.edipasquale.bitrise.source.session.SessionManager
import com.edipasquale.bitrise.validator.FieldValidator
import com.edipasquale.bitrise.validator.SimpleFieldValidator
import com.edipasquale.bitrise.viewmodel.auth.AuthViewModel
import com.edipasquale.bitrise.viewmodel.main.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppInjector {

    val appModule = module {

        viewModel {
            AuthViewModel(get(), get())
        }

        viewModel {
            MainViewModel(get(), androidApplication())
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

        single {
            SessionManager(androidContext())
        }

        factory {
            FirebaseAuth.getInstance()
        }

        factory {
            APIMainRepository(get(), get())
        }

        factory<MainRepository> {
            APIMainRepository(get(), get())
        }

        factory {
            MainLocalSource(get<AppDatabase>().dao())
        }

        single {
            Room.inMemoryDatabaseBuilder(
                androidApplication(),
                AppDatabase::class.java
            ).build()
        }

        factory<MainNetworkSource> {
            get<Retrofit>().create(MainNetworkSource::class.java)
        }


        single {
            Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(
                    OkHttpClient.Builder()
                        .addInterceptor(HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BODY
                        })
                        .addInterceptor {
                            val request = it.request()
                                .newBuilder()
                                .addHeader(
                                    "x-rapidapi-key",
                                    "00048a5f83mshe8ed26808120790p16d8ffjsn0202801de017"
                                )
                                .addHeader("x-rapidapi-host", "simpleanime.p.rapidapi.com")
                                .addHeader("useQueryString", "true")
                                .build()

                            it.proceed(request)
                        }
                        .build()
                )
                .build()
        }
    }

    fun setup(app: Application) = startKoin {
        androidContext(app)
        modules(appModule)
    }
}