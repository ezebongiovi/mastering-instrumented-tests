package com.edipasquale.bitrise.validator

interface FieldValidator {

    class FieldType {
        companion object {
            const val FIELD_EMAIL = 0
            const val FIELD_PASSWORD = 1
        }
    }

    /**
     * Validates a field and returns it's result
     *
     * @param input the field value being validated
     * @param fieldType the field type being validated
     */
    fun validateField(input: String, fieldType: Int): Int
}