package com.edipasquale.bitrise.source

import androidx.lifecycle.LiveData
import com.edipasquale.bitrise.dto.AuthDTO
import com.edipasquale.bitrise.dto.Either
import com.edipasquale.bitrise.dto.User

interface AuthSource {

    fun register(dto: AuthDTO): LiveData<Either<User, Int>>
}