package com.edipasquale.bitrise.validator

import com.edipasquale.bitrise.model.ERROR_EMAIL_FORMAT
import com.edipasquale.bitrise.model.ERROR_PASSWORD_FORMAT
import com.edipasquale.bitrise.model.ERROR_PASSWORD_LENGTH
import com.edipasquale.bitrise.model.SUCCESS
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class SimpleFieldValidatorTest {

    private val validator = SimpleFieldValidator()

    @Test
    fun testValidEmail() {
        assertEquals(
            SUCCESS, validator.validateField(
                "test@test.test",
                FIELD_EMAIL
            )
        )
    }

    @Test
    fun testEmailWithoutAtSymbol() {
        assertEquals(
            ERROR_EMAIL_FORMAT, validator.validateField(
                "test.test",
                FIELD_EMAIL
            )
        )
    }

    @Test
    fun testEmailWithoutDotSegment() {
        assertEquals(
            ERROR_EMAIL_FORMAT, validator.validateField(
                "test@test",
                FIELD_EMAIL
            )
        )
    }

    @Test
    fun testEmailWithInvalidDomain() {
        assertEquals(
            ERROR_EMAIL_FORMAT, validator.validateField(
                "test@test.t",
                FIELD_EMAIL
            )
        )
    }

    @Test
    fun testValidPassword() {
        assertEquals(
            SUCCESS, validator.validateField(
                "Admin_1234",
                FIELD_PASSWORD
            )
        )
    }

    @Test
    fun testWithoutSpecialCharacter() {
        assertEquals(
            ERROR_PASSWORD_FORMAT, validator.validateField(
                "Password123",
                FIELD_PASSWORD
            )
        )
    }

    @Test(expected = AssertionError::class)
    fun testInvalidFieldType() {
        validator.validateField("something", -99)
    }

    @Test
    fun testWithoutUpperChars() {
        assertEquals(
            ERROR_PASSWORD_FORMAT, validator.validateField(
                "password_123",
                FIELD_PASSWORD
            )
        )
    }

    @Test
    fun testWithoutNumbers() {
        assertEquals(
            ERROR_PASSWORD_FORMAT, validator.validateField(
                "Password#",
                FIELD_PASSWORD
            )
        )
    }

    @Test
    fun testInvalidLengthPassword() {
        assertEquals(
            ERROR_PASSWORD_LENGTH, validator.validateField(
                "abc",
                FIELD_PASSWORD
            )
        )
    }
}