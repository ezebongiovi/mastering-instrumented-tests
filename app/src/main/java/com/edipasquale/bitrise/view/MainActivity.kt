package com.edipasquale.bitrise.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.edipasquale.bitrise.R
import com.edipasquale.bitrise.databinding.ActivityMainBinding
import com.edipasquale.bitrise.model.*
import com.edipasquale.bitrise.validator.LENGTH_MIN_PASSWORD
import com.edipasquale.bitrise.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val mViewModel: MainViewModel by viewModel()
    private lateinit var _binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        
        setContentView(_binding.root)

        mViewModel.authResult.observe(this, Observer { model ->
            render(model)
        })

        _binding.buttonRegister.setOnClickListener {
            mViewModel.register(
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

                Toast.makeText(
                    this,
                    getString(R.string.feedback_register_success),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }
}
