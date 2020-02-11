package com.edipasquale.bitrise.validator

import com.edipasquale.bitrise.model.ERROR_EMAIL_FORMAT
import com.edipasquale.bitrise.model.ERROR_PASSWORD_FORMAT
import com.edipasquale.bitrise.model.ERROR_PASSWORD_LENGTH
import com.edipasquale.bitrise.model.SUCCESS

/**
 * In this regex, we have added some restrictions on username part of email address.
 * Restrictions in above regex are:
 *
 * 1) A-Z characters allowed
 * 2) a-z characters allowed
 * 3) 0-9 numbers allowed
 * 4) Additionally email may contain only dot(.), dash(-) and underscore(_)
 * 5) Rest all characters are not allowed
 */
private const val REGEX_EMAIL_FORMAT =
    "^[\\w!#\$%&’*+=?`{|}~^-]+(?:\\.[\\w!#\$%&’*+=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}\$"

/**
 * Regex for password validation. The password requires:
 *
 * 1) The presence of at least one lowercase letter.
 * 2) The presence of at least one digit i.e. 0-9.
 * 3) The presence of at least one special character.
 * 4) The presence of at least one capital letter.
 */
private const val REGEX_PASSWORD_FORMAT = "((?=.*[a-z])(?=.*\\d)(?=.*[@#\$._%])(?=.*[A-Z]).+)"

const val LENGTH_MIN_PASSWORD = 6

class SimpleFieldValidator : FieldValidator {

    /**
     * Validates that the email matches the email regex validation.
     *
     * @param email The email being validated
     */
    private fun isValidEmail(email: String): Int {
        return if (REGEX_EMAIL_FORMAT.toRegex().matches(email))
            SUCCESS
        else
            ERROR_EMAIL_FORMAT
    }

    /**
     * Validates that the password matches the password validation regex
     *
     * @param password the password being validated
     */
    private fun isValidPassword(password: String): Int {

        return when {
            // Doesn't match min length
            password.length < LENGTH_MIN_PASSWORD -> ERROR_PASSWORD_LENGTH

            // Matches regex
            REGEX_PASSWORD_FORMAT.toRegex().matches(password) -> SUCCESS

            // Doesn't match regex
            else -> ERROR_PASSWORD_FORMAT
        }
    }

    override fun validateField(input: String, fieldType: Int): Int {
        return when (fieldType) {
            FIELD_EMAIL -> isValidEmail(input)
            FIELD_PASSWORD -> isValidPassword(input)
            else -> throw AssertionError("Invalid field type")
        }
    }
}