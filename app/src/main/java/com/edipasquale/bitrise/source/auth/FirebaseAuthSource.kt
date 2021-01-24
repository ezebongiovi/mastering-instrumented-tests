package com.edipasquale.bitrise.source.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.edipasquale.bitrise.dto.EmailPasswordAuth
import com.edipasquale.bitrise.dto.User
import com.edipasquale.bitrise.dto.core.Either
import com.edipasquale.bitrise.model.ERROR_UNKNOWN
import com.google.firebase.auth.FirebaseAuth
import org.koin.java.KoinJavaComponent.inject

class FirebaseAuthSource : AuthSource {
    private val _firebaseAuth: FirebaseAuth by inject(clazz = FirebaseAuth::class.java)

    override fun register(dto: EmailPasswordAuth): LiveData<Either<User, Int>> {
        val liveData = MutableLiveData<Either<User, Int>>()

        _firebaseAuth.createUserWithEmailAndPassword(dto.email, dto.password)
            .addOnFailureListener {

            }
            .addOnSuccessListener {
                it.user?.let { user ->
                    liveData.postValue(Either.Data(User(email = user.email!!)))
                } ?: run { liveData.postValue(Either.Error(ERROR_UNKNOWN)) }
            }

        return liveData
    }
}