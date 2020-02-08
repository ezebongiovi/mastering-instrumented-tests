package com.edipasquale.bitrise.repository

import androidx.lifecycle.LiveData
import com.edipasquale.bitrise.dto.AuthDTO
import com.edipasquale.bitrise.model.MainModel

interface AuthRepository {

    fun register(authDTO: AuthDTO): LiveData<MainModel>
}