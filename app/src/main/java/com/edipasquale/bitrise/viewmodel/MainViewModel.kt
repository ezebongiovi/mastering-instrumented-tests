package com.edipasquale.bitrise.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.edipasquale.bitrise.dto.AuthDTO
import com.edipasquale.bitrise.model.ERROR_PASSWORD_MATCH
import com.edipasquale.bitrise.model.MainModel
import com.edipasquale.bitrise.model.SUCCESS
import com.edipasquale.bitrise.repository.AuthRepository
import com.edipasquale.bitrise.validator.FIELD_EMAIL
import com.edipasquale.bitrise.validator.FIELD_PASSWORD
import com.edipasquale.bitrise.validator.FieldValidator

class MainViewModel(private val validator: FieldValidator, private val repository: AuthRepository) :
    ViewModel() {

    private var _authResult: LiveData<MainModel>? = null
    val authResult = MediatorLiveData<MainModel>()

    fun register(email: String, password: String, passwordConfirmation: String) {
        if (password == passwordConfirmation) {

            val emailResult = validator
                .validateField(email, FIELD_EMAIL)

            val passwordResult = validator
                .validateField(password, FIELD_PASSWORD)

            
            val liveData = MutableLiveData<MainModel>()

            if (emailResult != SUCCESS)
                // Email error
                liveData.postValue(MainModel(error = emailResult))
            else if (passwordResult != SUCCESS)
                // Password error
                liveData.postValue(MainModel(error = passwordResult))
            else
                // No errors
                addSource(repository.register(AuthDTO(email, password)))

            addSource(liveData)

        } else {
            val liveData = MutableLiveData<MainModel>()
            liveData.postValue(MainModel(error = ERROR_PASSWORD_MATCH))

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
