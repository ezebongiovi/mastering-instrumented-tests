package com.edipasquale.bitrise.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.edipasquale.bitrise.dto.EmailPasswordAuth
import com.edipasquale.bitrise.dto.core.Either
import com.edipasquale.bitrise.model.MainModel
import com.edipasquale.bitrise.source.auth.AuthSource

class SimpleAuthRepository(private val source: AuthSource) : AuthRepository {

    override fun register(authDTO: EmailPasswordAuth): LiveData<MainModel> =
        Transformations.map(source.register(authDTO)) { response ->
            when (response) {
                is Either.Data -> MainModel(data = response.data)
                is Either.Error -> MainModel(error = response.error)
            }
        }
}