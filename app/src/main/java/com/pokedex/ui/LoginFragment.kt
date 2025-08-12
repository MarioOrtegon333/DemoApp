package com.pokedex.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.demoapp.databinding.FragmentLoginBinding
import androidx.navigation.fragment.findNavController
import com.example.demoapp.R


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
            viewModel.login(username, password)
        }
        viewModel.loginResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                // Guardar sesión en SharedPreferences
                val prefs = requireContext().getSharedPreferences("session", android.content.Context.MODE_PRIVATE)
                prefs.edit().putBoolean("isLoggedIn", true).putString("username", binding.etUsername.text.toString()).apply()
                // Navegar al MainFragment
                findNavController().navigate(R.id.pokedexFragment)
            } else {
                android.widget.Toast.makeText(requireContext(), "Usuario o contraseña incorrectos", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
