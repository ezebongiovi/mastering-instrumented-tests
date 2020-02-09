package com.edipasquale.bitrise.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.edipasquale.bitrise.dto.AuthDTO
import com.edipasquale.bitrise.dto.core.Either
import com.edipasquale.bitrise.dto.User

class SimpleAuthSource : AuthSource {

    override fun register(dto: AuthDTO): LiveData<Either<User, Int>> {
        val liveData = MutableLiveData<Either<User, Int>>()

        liveData.postValue(Either.Data(User(dto.email, dto.password)))

        return liveData
    }
}