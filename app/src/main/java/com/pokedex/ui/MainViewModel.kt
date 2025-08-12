package com.pokedex.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData

class MainViewModel : ViewModel() {
    private val _welcomeMessage = MutableLiveData<String>()
    val welcomeMessage: LiveData<String> = _welcomeMessage

    fun setWelcomeMessage(username: String) {
        _welcomeMessage.value = "Bienvenido, $username!"
    }
}
