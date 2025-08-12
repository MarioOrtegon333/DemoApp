package com.example.demoapp.presentation.ui.login

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.example.demoapp.domain.usecase.LoginWithFirebaseUseCase

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginWithFirebaseUseCase: LoginWithFirebaseUseCase
) : ViewModel() {
    val loginResultAuth = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()

    fun loginAuth(email: String, password: String) {
        loginWithFirebaseUseCase.execute(email, password) { success, error ->
            loginResultAuth.value = success
            errorMessage.value = error
        }
    }
}
