package com.edipasquale.bitrise.source.auth

import androidx.lifecycle.LiveData
import com.edipasquale.bitrise.dto.EmailPasswordAuth
import com.edipasquale.bitrise.dto.User
import com.edipasquale.bitrise.dto.core.Either
import kotlinx.coroutines.flow.Flow

interface AuthSource {

    fun register(dto: EmailPasswordAuth): LiveData<Either<User, Int>>
}