package com.edipasquale.bitrise.repository.auth

import androidx.lifecycle.LiveData
import com.edipasquale.bitrise.dto.EmailPasswordAuth
import com.edipasquale.bitrise.model.MainModel

interface AuthRepository {

    fun register(authDTO: EmailPasswordAuth): LiveData<MainModel>
}