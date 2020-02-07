package com.edipasquale.bitrise.model

import com.edipasquale.bitrise.dto.User

data class MainModel(val data: User? = null, val error: Int? = null) {

    class Result {
        companion object {
            const val SUCCESS = 0
            const val ERROR_EMAIL_FORMAT = -1
            const val ERROR_PASSWORD_LENGTH = -2
            const val ERROR_PASSWORD_FORMAT = -3
            const val ERROR_PASSWORD_MATCH = -4
        }
    }

    fun isSuccess(): Boolean = error == null
}