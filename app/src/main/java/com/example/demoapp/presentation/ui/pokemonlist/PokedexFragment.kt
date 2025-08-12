package com.example.demoapp.presentation.ui.pokemonlist


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demoapp.R
import com.example.demoapp.databinding.FragmentPokedexBinding
import com.example.demoapp.presentation.ui.pokemonlist.adapter.PokemonAdapter


class PokedexFragment : Fragment() {
    private var backPressedCount = 0
    private lateinit var recyclerViewPokedex: RecyclerView
    private lateinit var adapter: PokemonAdapter


    override fun onResume() {
        super.onResume()
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            backPressedCount++
            if (backPressedCount >= 2) {
                showLogoutDialog()
            } else {
                Toast.makeText(requireContext(), "Presiona otra vez para cerrar sesión", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLogoutDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Cerrar sesión")
            .setMessage("¿Seguro que deseas cerrar sesión?")
            .setPositiveButton("Sí") { _, _ ->
                com.example.demoapp.utils.SessionManager.clearSession(requireContext())
                // Lanzar LoginActivity y terminar la actividad actual
                val intent = android.content.Intent(requireContext(), com.example.demoapp.LoginActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
            .setNegativeButton("No") { _, _ -> backPressedCount = 0 }
            .show()
    }

    private var _binding: FragmentPokedexBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PokedexViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPokedexBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews()

         adapter = PokemonAdapter(emptyList()) { pokemon : Pokemon ->
            val bundle = Bundle().apply {
                putString("pokemonId", pokemon.id.toString())
                putBoolean("refresh", true)
            }
           findNavController().navigate(R.id.pokemonDetailFragment, bundle)
        }

        binding.rvPokedex.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = adapter
        }

        // SwipeRefreshLayout: refresca el listado
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadPokemons()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressLoader.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.swipeRefreshLayout.isRefreshing = isLoading
        }

        // Loader: observar isLoading
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressLoader.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.pokemonList.observe(viewLifecycleOwner) { pokemons ->
            adapter = PokemonAdapter(pokemons) { pokemon : Pokemon ->
                val bundle = Bundle().apply {
                    putString("pokemonId", pokemon.id.toString())
                }
                findNavController().navigate(R.id.pokemonDetailFragment, bundle)
            }

            binding.rvPokedex.adapter = adapter
        }

        binding.btnNext.setOnClickListener {
            viewModel.nextPage()
        }
        binding.btnPrevious.setOnClickListener {
            viewModel.previousPage()
        }

        viewModel.loadPokemons()
    }

    private fun initializeViews() {
        recyclerViewPokedex = binding.rvPokedex
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
