package com.example.demoapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.demoapp.presentation.ui.login.LoginViewModel
import com.example.demoapp.utils.SessionManager

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: com.example.demoapp.databinding.ActivityLoginBinding
    private val viewModel: LoginViewModel by lazy { LoginViewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = com.example.demoapp.databinding.ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val isLoggedIn = SessionManager.isLoggedIn(this)

        if (isLoggedIn) {
            startActivity(android.content.Intent(this, MainActivity::class.java))
            finish()
            return
        }

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            viewModel.login(username, password)
        }

        viewModel.loginResult.observe(this) { success ->
            if (success) {
                SessionManager.saveSession(this, binding.etUsername.text.toString())
                startActivity(android.content.Intent(this, MainActivity::class.java))
                finish()
            } else {
                android.widget.Toast.makeText(this, "Usuario o contraseña incorrectos", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }
}