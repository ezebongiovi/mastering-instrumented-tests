package com.edipasquale.bitrise.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.edipasquale.bitrise.R
import com.edipasquale.bitrise.model.*
import com.edipasquale.bitrise.validator.LENGTH_MIN_PASSWORD
import com.edipasquale.bitrise.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val mViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mViewModel.authResult.observe(this, Observer { model ->
            render(model)
        })

        buttonRegister.setOnClickListener {
            mViewModel.register(
                fieldEmail.text.toString(),
                fieldPassword.text.toString(),
                fieldPasswordConfirmation.text.toString()
            )
        }
    }

    private fun render(model: MainModel) {

        when (model.error) {
            ERROR_EMAIL_FORMAT -> fieldEmail.error = getString(R.string.feedback_error_email_format)

            ERROR_PASSWORD_FORMAT -> fieldPassword.error =
                getString(R.string.feedback_error_password_format)

            ERROR_PASSWORD_LENGTH -> fieldPassword.error =
                getString(R.string.feedback_error_password_length, LENGTH_MIN_PASSWORD)

            ERROR_PASSWORD_MATCH -> fieldPasswordConfirmation.error =
                getString(R.string.feedback_error_password_match)

            else -> {
                fieldEmail.error = null
                fieldPassword.error = null
                fieldPasswordConfirmation.error = null

                Toast.makeText(
                    this,
                    getString(R.string.feedback_register_success),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }
}
