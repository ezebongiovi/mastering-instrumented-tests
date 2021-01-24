package com.edipasquale.bitrise.view.activity.main

import android.os.Bundle
import com.edipasquale.bitrise.databinding.ActivityMainBinding
import com.edipasquale.bitrise.dto.User
import com.edipasquale.bitrise.view.activity.auth.core.AuthActivity

class MainActivity: AuthActivity() {

    private lateinit var _binding : ActivityMainBinding

    override fun onAuthenticated(savedInstanceState: Bundle?, user: User) {
        _binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(_binding.root)
    }

    override fun shouldAuthenticate() = true
}