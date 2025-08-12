package com.example.demoapp.domain.usecase

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class LoginWithFirebaseUseCase @Inject constructor(
    private val auth: FirebaseAuth
) {
    fun execute(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }
}
