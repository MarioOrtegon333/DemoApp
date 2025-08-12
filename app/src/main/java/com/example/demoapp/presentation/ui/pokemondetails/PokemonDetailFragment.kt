package com.example.demoapp.presentation.ui.pokemondetails

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import androidx.fragment.app.viewModels
import com.example.demoapp.databinding.FragmentPokemonDetailBinding
import com.example.demoapp.utils.TypeColorEnum
import com.squareup.picasso.Picasso
import com.example.demoapp.domain.model.PokemonDetail


@AndroidEntryPoint
class PokemonDetailFragment : Fragment() {
    private var _binding: FragmentPokemonDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PokemonDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPokemonDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pokemonId = arguments?.getString("pokemonId") ?: "1"
        viewModel.loadPokemonDetail(pokemonId)

        // Loader: observar isLoading
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->

            binding.progressLoaderDetail.visibility = if (isLoading) View.VISIBLE else View.GONE

            // Oculta todos los datos mientras carga
            val visible = !isLoading
            hideShowDetails(visible)
        }

        viewModel.pokemonDetail.observe(viewLifecycleOwner) { detail ->
            detail?.let {
                renderDetails(it)
                renderStats(it)
                renderTypesAsBadges(it)
            }
        }

    }

    // Renderizar detalles del Pokémon
    private fun renderDetails(pokemonDetail: PokemonDetail) {
        val heightMts = pokemonDetail.height / 10.0
        val weightKg = pokemonDetail.weight / 10.0
        val tvPokemonName = binding.tvPokemonName
        val tvPokemonNumber = binding.tvPokemonNumber
        val tvPokemonHeight = binding.tvPokemonHeight
        val tvPokemonWeight = binding.tvPokemonWeight
        val tvPokemonAbilities = binding.tvPokemonAbilities


        tvPokemonName.text = pokemonDetail.name
        tvPokemonNumber.text = "#%03d".format(pokemonDetail.id)
        tvPokemonHeight.text = "%.1f mts".format(heightMts)
        tvPokemonWeight.text = "%.1f kg".format(weightKg)
        tvPokemonAbilities.text = "${pokemonDetail.abilities.joinToString(", ")}"
        Picasso.get().load(pokemonDetail.imageUrl).into(binding.ivPokemonImage)

    }

    // Renderizar estadísticas del Pokémon
    private fun renderStats(pokemonDetail: PokemonDetail) {

    val statMap = pokemonDetail.stats.associateBy { pokemonDetail.name }
    binding.tvStatHp.text = "${statMap["hp"]?.baseStat ?: "-"}"
    binding.tvStatAttack.text = "${statMap["attack"]?.baseStat ?: "-"}"
    binding.tvStatDefense.text = "${statMap["defense"]?.baseStat ?: "-"}"
    binding.tvStatSpAttack.text = "${statMap["special-attack"]?.baseStat ?: "-"}"
    binding.tvStatSpDefense.text = "${statMap["special-defense"]?.baseStat ?: "-"}"
    binding.tvStatSpeed.text = "${statMap["speed"]?.baseStat ?: "-"}"

    }

    // Renderizar tipos del pokémon como badges
    @RequiresApi(Build.VERSION_CODES.M)
    private fun renderTypesAsBadges(pokemonDetail: PokemonDetail) {

        binding.layoutPokemonTypesDetail.removeAllViews()
        val context = binding.layoutPokemonTypesDetail.context
        for (type in pokemonDetail.types) {
            val badge = android.widget.TextView(context)
            badge.text = type
            badge.setPadding(32, 12, 32, 12)
            badge.setTextColor(android.graphics.Color.WHITE)
            badge.textSize = 14f
            badge.background = getBadgeDrawable(context, getTypeColorRes(type.lowercase()))
            val params = android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(12, 0, 12, 0)
            badge.layoutParams = params
            binding.layoutPokemonTypesDetail.addView(badge)
        }

    }

    // Ocultar o mostrar detalles del Pokémon
    private fun hideShowDetails(visible: Boolean) {
        binding.ivPokemonImage.visibility = if (visible) View.VISIBLE else View.GONE
        binding.tvPokemonName.visibility = if (visible) View.VISIBLE else View.GONE
        binding.tvPokemonNumber.visibility = if (visible) View.VISIBLE else View.GONE
        binding.tvPokemonHeight.visibility = if (visible) View.VISIBLE else View.GONE
        binding.tvPokemonWeight.visibility = if (visible) View.VISIBLE else View.GONE
        binding.tvPokemonAbilities.visibility = if (visible) View.VISIBLE else View.GONE
        binding.layoutPokemonTypesDetail.visibility = if (visible) View.VISIBLE else View.GONE
        binding.cardDetails.visibility = if (visible) View.VISIBLE else View.GONE
        binding.cardStats.visibility = if (visible) View.VISIBLE else View.GONE
        binding.cardHabilities.visibility = if (visible) View.VISIBLE else View.GONE

    }


    // Crear un drawable de badge con el color del tipo
    @RequiresApi(Build.VERSION_CODES.M)
    private fun getBadgeDrawable(context: android.content.Context, colorRes: Int): android.graphics.drawable.Drawable {
        val radius = 32f
        val shape = android.graphics.drawable.GradientDrawable()
        shape.shape = android.graphics.drawable.GradientDrawable.RECTANGLE
        shape.cornerRadius = radius
        shape.setColor(context.getColor(colorRes))
        return shape
    }

    // Obtener el recurso de color según el tipo del Pokémon
    private fun getTypeColorRes(type: String): Int {
        return try {
            TypeColorEnum.valueOf(type.uppercase()).resId
        } catch (e: Exception) {
           TypeColorEnum.UNKNOWN.resId
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
