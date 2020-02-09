package com.edipasquale.bitrise.model

import com.edipasquale.bitrise.dto.User

const val SUCCESS = 0
const val ERROR_EMAIL_FORMAT = -1
const val ERROR_PASSWORD_LENGTH = -2
const val ERROR_PASSWORD_FORMAT = -3
const val ERROR_PASSWORD_MATCH = -4

data class MainModel(val data: User? = null, val error: Int? = null) {

    fun isSuccess(): Boolean = error == null
}