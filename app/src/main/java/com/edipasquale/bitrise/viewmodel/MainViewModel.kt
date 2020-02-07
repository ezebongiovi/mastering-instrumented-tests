package com.edipasquale.bitrise.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.edipasquale.bitrise.dto.AuthDTO
import com.edipasquale.bitrise.model.MainModel
import com.edipasquale.bitrise.source.AuthSource
import com.edipasquale.bitrise.validator.FieldValidator

class MainViewModel(private val validator: FieldValidator, private val source: AuthSource) :
    ViewModel() {

    private var _authResult: LiveData<MainModel>? = null
    val authResult = MediatorLiveData<MainModel>()

    fun register(email: String, password: String, passwordConfirmation: String) {
        if (password == passwordConfirmation) {

            val emailResult = validator
                .validateField(email, FieldValidator.FieldType.FIELD_EMAIL)

            val passwordResult = validator
                .validateField(password, FieldValidator.FieldType.FIELD_PASSWORD)

            if (emailResult == MainModel.Result.SUCCESS && passwordResult == MainModel.Result.SUCCESS)
                addSource(source.register(AuthDTO(email, password)))
            else {
                val liveData = MutableLiveData<MainModel>()

                if (emailResult != MainModel.Result.SUCCESS)
                    liveData.postValue(MainModel(error = emailResult))
                else if (passwordResult != MainModel.Result.SUCCESS)
                    liveData.postValue(MainModel(error = passwordResult))

                addSource(liveData)
            }
        } else {
            val liveData = MutableLiveData<MainModel>()
            liveData.postValue(MainModel(error = MainModel.Result.ERROR_PASSWORD_MATCH))

            addSource(liveData)
        }
    }

    private fun addSource(liveData: LiveData<MainModel>) {
        if (_authResult != null)
            authResult.removeSource(_authResult!!)

        _authResult = liveData
        authResult.addSource(_authResult!!) { model ->
            authResult.postValue(model)
        }
    }
}