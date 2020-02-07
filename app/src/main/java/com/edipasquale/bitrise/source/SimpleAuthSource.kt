package com.edipasquale.bitrise.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.edipasquale.bitrise.dto.AuthDTO
import com.edipasquale.bitrise.dto.User
import com.edipasquale.bitrise.model.MainModel

class SimpleAuthSource : AuthSource {

    override fun register(dto: AuthDTO): LiveData<MainModel> {
        val liveData = MutableLiveData<MainModel>()

        liveData.postValue(MainModel(data = User(dto.email)))

        return liveData
    }
}