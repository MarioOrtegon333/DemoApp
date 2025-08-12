package com.example.demoapp.presentation.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {
    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> = _loginResult

    private val auth:FirebaseAuth = FirebaseAuth.getInstance()
    val loginResultAuth = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()


    fun loginAuth(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Inicio de sesión exitoso
                    loginResultAuth.value = true
                } else {
                    // Error en el inicio de sesión
                    errorMessage.value = task.exception?.message
                    val error = task.exception?.message
                    loginResultAuth.value = false

                }
            }
    }



    fun login(username: String, password: String) {
        // Usuario y contraseña estáticos
        _loginResult.value = (username == "admin" && password == "admin")
    }
}
