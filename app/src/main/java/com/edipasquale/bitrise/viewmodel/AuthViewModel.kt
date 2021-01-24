package com.edipasquale.bitrise.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.edipasquale.bitrise.dto.EmailPasswordAuth
import com.edipasquale.bitrise.model.ERROR_PASSWORD_MATCH
import com.edipasquale.bitrise.model.MainModel
import com.edipasquale.bitrise.model.SUCCESS
import com.edipasquale.bitrise.repository.AuthRepository
import com.edipasquale.bitrise.validator.FIELD_EMAIL
import com.edipasquale.bitrise.validator.FIELD_PASSWORD
import com.edipasquale.bitrise.validator.FieldValidator

class AuthViewModel(private val validator: FieldValidator, private val repository: AuthRepository) :
    ViewModel() {

    private val _authResult = MediatorLiveData<MainModel>()
    val authResult: LiveData<MainModel> =_authResult

    fun register(email: String, password: String, passwordConfirmation: String) {
        if (password == passwordConfirmation) {

            val emailResult = validator
                .validateField(email, FIELD_EMAIL)

            val passwordResult = validator
                .validateField(password, FIELD_PASSWORD)

            
            when {
                emailResult != SUCCESS
                    // Email error
                -> _authResult.postValue(MainModel(error = emailResult))
                passwordResult != SUCCESS
                    // Password error
                -> _authResult.postValue(MainModel(error = passwordResult))
                else
                    // No errors
                -> _authResult.addSource(repository.register(EmailPasswordAuth(email, password))) {
                    _authResult.postValue(it)
                }
            }

        } else {
            _authResult.postValue(MainModel(error = ERROR_PASSWORD_MATCH))
        }
    }
}
