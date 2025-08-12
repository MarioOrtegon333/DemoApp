package com.example.demoapp.presentation.ui.pokemonlist


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.demoapp.utils.SessionManager


class PokedexFragment : Fragment() {
    private var backPressedCount = 0
    private lateinit var recyclerViewPokedex: RecyclerView
    private lateinit var adapter: PokemonAdapter
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
        setUpRecyclerView()
        observeViewModel()

        viewModel.loadPokemons()
    }

    private fun setUpRecyclerView() {

        recyclerViewPokedex = binding.rvPokedex

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
    }

    private fun initializeViews() {

        // SwipeRefreshLayout: refresca el listado
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadPokemons()
        }

        //Boton siguiente
        binding.btnNext.setOnClickListener {
            viewModel.nextPage()
        }

        //Boton anterior
        binding.btnPrevious.setOnClickListener {
            viewModel.previousPage()
        }
    }

    private fun observeViewModel() {

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressLoader.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.swipeRefreshLayout.isRefreshing = isLoading
        }

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

    }

    private fun showLogoutDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.dialog_title_close_session))
            .setMessage(getString(R.string.dialog_message_close_session))
            .setPositiveButton(getString(R.string.text_yes)) { _, _ ->
                SessionManager.clearSession(requireContext())
                // Lanzar LoginActivity y terminar la actividad actual
                val intent = android.content.Intent(requireContext(), com.example.demoapp.LoginActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
            .setNegativeButton(getString(R.string.text_no)) { _, _ -> backPressedCount = 0 }
            .show()
    }

    override fun onResume() {
        super.onResume()
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            showLogoutDialog()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
