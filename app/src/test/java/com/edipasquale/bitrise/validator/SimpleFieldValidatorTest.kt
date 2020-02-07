package com.edipasquale.bitrise.validator

import com.edipasquale.bitrise.model.MainModel
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
            MainModel.Result.SUCCESS, validator.validateField(
                "test@test.test",
                FieldValidator.FieldType.FIELD_EMAIL
            )
        )
    }

    @Test
    fun testEmailWithoutAtSymbol() {
        assertEquals(
            MainModel.Result.ERROR_EMAIL_FORMAT, validator.validateField(
                "test.test",
                FieldValidator.FieldType.FIELD_EMAIL
            )
        )
    }

    @Test
    fun testEmailWithoutDotSegment() {
        assertEquals(
            MainModel.Result.ERROR_EMAIL_FORMAT, validator.validateField(
                "test@test",
                FieldValidator.FieldType.FIELD_EMAIL
            )
        )
    }

    @Test
    fun testEmailWithInvalidDomain() {
        assertEquals(
            MainModel.Result.ERROR_EMAIL_FORMAT, validator.validateField(
                "test@test.t",
                FieldValidator.FieldType.FIELD_EMAIL
            )
        )
    }

    @Test
    fun testValidPassword() {
        assertEquals(
            MainModel.Result.SUCCESS, validator.validateField(
                "Admin_1234",
                FieldValidator.FieldType.FIELD_PASSWORD
            )
        )
    }

    @Test
    fun testWithoutSpecialCharacter() {
        assertEquals(
            MainModel.Result.ERROR_PASSWORD_FORMAT, validator.validateField(
                "Password123",
                FieldValidator.FieldType.FIELD_PASSWORD
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
            MainModel.Result.ERROR_PASSWORD_FORMAT, validator.validateField(
                "password_123",
                FieldValidator.FieldType.FIELD_PASSWORD
            )
        )
    }

    @Test
    fun testWithoutNumbers() {
        assertEquals(
            MainModel.Result.ERROR_PASSWORD_FORMAT, validator.validateField(
                "Password#",
                FieldValidator.FieldType.FIELD_PASSWORD
            )
        )
    }

    @Test
    fun testInvalidLengthPassword() {
        assertEquals(
            MainModel.Result.ERROR_PASSWORD_LENGTH, validator.validateField(
                "abc",
                FieldValidator.FieldType.FIELD_PASSWORD
            )
        )
    }
}