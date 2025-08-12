package com.pokedex.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.demoapp.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Ejemplo: mostrar mensaje de bienvenida
        val username = "admin" // Aquí podrías obtener el usuario real desde argumentos o ViewModel compartido
        viewModel.setWelcomeMessage(username)
        viewModel.welcomeMessage.observe(viewLifecycleOwner) { msg ->
            binding.tvWelcome.text = msg
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
