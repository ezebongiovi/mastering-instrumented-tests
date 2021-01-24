package com.edipasquale.bitrise.source.session

import android.app.Activity
import android.content.Context
import com.edipasquale.bitrise.dto.User
import com.google.gson.Gson

private const val PREF_ACC = "pref-acc"
private const val PREF_USER = "pref-user"

class SessionManager(private val context: Context) {

    fun saveUser(user: User) {
        context.getSharedPreferences(PREF_ACC, Activity.MODE_PRIVATE)
            .edit().putString(PREF_USER, Gson().toJson(user)).apply()
    }

    fun getUser(): User? {
        val userJson = context.getSharedPreferences(PREF_ACC, Activity.MODE_PRIVATE)
            .getString(PREF_USER, null)

        return if (userJson == null)
            null
        else
            Gson().fromJson(userJson, User::class.java)
    }
}