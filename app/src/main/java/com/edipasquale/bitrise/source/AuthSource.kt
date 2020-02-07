package com.edipasquale.bitrise.source

import androidx.lifecycle.LiveData
import com.edipasquale.bitrise.dto.AuthDTO
import com.edipasquale.bitrise.model.MainModel

interface AuthSource {

    fun register(dto: AuthDTO): LiveData<MainModel>
}