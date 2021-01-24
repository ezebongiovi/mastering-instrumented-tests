package com.edipasquale.bitrise.view.activity.auth.core

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.edipasquale.bitrise.dto.User
import com.edipasquale.bitrise.source.session.SessionManager
import com.edipasquale.bitrise.view.activity.auth.RegisterActivity
import org.koin.android.ext.android.inject

abstract class AuthActivity : AppCompatActivity() {

    protected val sessionManager: SessionManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!shouldAuthenticate())
            return

        val user = sessionManager.getUser()

        if (user == null)
            startActivity(
                Intent(this, RegisterActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
        else
            onAuthenticated(savedInstanceState, user)
    }

    abstract fun onAuthenticated(savedInstanceState: Bundle?, user: User)

    abstract fun shouldAuthenticate(): Boolean
}