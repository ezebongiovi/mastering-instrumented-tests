package com.edipasquale.bitrise.validator

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class FieldValidatorTest {

    private val validator = FieldValidator()

    @Test
    fun testValidEmail() {
        assertEquals(FieldValidator.SUCCESS, validator.isValidEmail("test@test.test"))
    }

    @Test
    fun testInvalidEmail() {
        assertEquals(FieldValidator.ERROR_EMAIL_FORMAT, validator.isValidEmail("some"))
    }

    @Test
    fun testValidPassword() {
        assertEquals(FieldValidator.SUCCESS, validator.isValidPassword("Admin_1234"))
    }

    @Test
    fun testInvalidFormatPassword() {
        assertEquals(FieldValidator.ERROR_PASSWORD_FORMAT, validator.isValidPassword("password"))
    }

    @Test
    fun testInvalidLengthPassword() {
        assertEquals(FieldValidator.ERROR_PASSWORD_LENGTH, validator.isValidPassword("abc"))
    }
}