package com.example.demoapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dagger.hilt.android.AndroidEntryPoint
import androidx.activity.viewModels
import com.example.demoapp.presentation.ui.login.LoginViewModel
import com.example.demoapp.utils.SessionManager
import com.example.demoapp.databinding.ActivityLoginBinding

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        validateIsLogged()
        initializeViews()
        observeViewModel()
    }

    //Se valida que se este loguead y redirige directo al listado
    private fun validateIsLogged() {
        val isLoggedIn = SessionManager.isLoggedIn(this)

        if (isLoggedIn) {
            startActivity(android.content.Intent(this, MainActivity::class.java))
            finish()
            return
        }
    }

    //Observers
    private fun observeViewModel() {

        //Observer del autenticacion de Firebase
        viewModel.loginResultAuth.observe(this) { success ->
            if (success) {
                SessionManager.saveSession(this, binding.etUsername.text.toString())
                startActivity(android.content.Intent(this, MainActivity::class.java))
                finish()
            } else {
                android.widget.Toast.makeText(
                    this,
                    getString(R.string.login_message_error),
                    android.widget.Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    //Eventos del Activity
    private fun initializeViews() {

        //Evento del boton de login
        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            viewModel.loginAuth(username, password)
        }
    }
}