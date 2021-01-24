package com.edipasquale.bitrise.view.activity.auth

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.edipasquale.bitrise.R
import com.edipasquale.bitrise.databinding.ActivityRegisterBinding
import com.edipasquale.bitrise.dto.User
import com.edipasquale.bitrise.model.*
import com.edipasquale.bitrise.validator.LENGTH_MIN_PASSWORD
import com.edipasquale.bitrise.view.activity.auth.core.AuthActivity
import com.edipasquale.bitrise.view.activity.main.MainActivity
import com.edipasquale.bitrise.viewmodel.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AuthActivity() {

    private val _viewModel: AuthViewModel by viewModel()
    private lateinit var _binding : ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (sessionManager.getUser() != null) {
            startMainActivity()

            return
        }

        _binding = ActivityRegisterBinding.inflate(layoutInflater)

        setContentView(_binding.root)

        _viewModel.authResult.observe(this, Observer { model ->
            render(model)
        })

        _binding.buttonRegister.setOnClickListener {
            _viewModel.register(
                _binding.fieldEmail.text.toString(),
                _binding.fieldPassword.text.toString(),
                _binding.fieldPasswordConfirmation.text.toString()
            )
        }
    }

    private fun render(model: MainModel) {

        when (model.error) {
            ERROR_EMAIL_FORMAT -> _binding.fieldEmail.error = getString(R.string.feedback_error_email_format)

            ERROR_PASSWORD_FORMAT -> _binding.fieldPassword.error =
                getString(R.string.feedback_error_password_format)

            ERROR_PASSWORD_LENGTH -> _binding.fieldPassword.error =
                getString(R.string.feedback_error_password_length, LENGTH_MIN_PASSWORD)

            ERROR_PASSWORD_MATCH -> _binding.fieldPasswordConfirmation.error =
                getString(R.string.feedback_error_password_match)

            else -> {
                _binding.fieldEmail.error = null
                _binding.fieldPassword.error = null
                _binding.fieldPasswordConfirmation.error = null
            }
        }

        model.data?.let { user ->
            sessionManager.saveUser(user)

            startMainActivity()
        }
    }

    override fun onAuthenticated(savedInstanceState: Bundle?, user: User) {
        startMainActivity()
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java).addFlags(
            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        ))
    }

    override fun shouldAuthenticate() = false
}
