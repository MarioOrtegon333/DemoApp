package com.pokedex.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.demoapp.databinding.FragmentPokemonDetailBinding
import com.squareup.picasso.Picasso


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
        viewModel.pokemonDetail.observe(viewLifecycleOwner) { detail ->
            detail?.let {
                binding.tvPokemonName.text = it.name
                binding.tvPokemonNumber.text = "#%03d".format(it.id)
                val alturaMts = it.height / 10.0
                val pesoKg = it.weight / 10.0
                binding.tvPokemonHeight.text = "%.1f mts".format(alturaMts)
                binding.tvPokemonWeight.text = "%.1f kg".format(pesoKg)
                binding.tvPokemonAbilities.text = "${it.abilities.joinToString(", ")}"
                Picasso.get().load(it.imageUrl).into(binding.ivPokemonImage)

                // Estadísticas principales
                val statMap = it.stats.associateBy { statItem -> statItem.stat.name }
                binding.tvStatHp.text = "${statMap["hp"]?.base_stat ?: "-"}"
                binding.tvStatAttack.text = "${statMap["attack"]?.base_stat ?: "-"}"
                binding.tvStatDefense.text = "${statMap["defense"]?.base_stat ?: "-"}"
                binding.tvStatSpAttack.text = "${statMap["special-attack"]?.base_stat ?: "-"}"
                binding.tvStatSpDefense.text = "${statMap["special-defense"]?.base_stat ?: "-"}"
                binding.tvStatSpeed.text = "${statMap["speed"]?.base_stat ?: "-"}"

                // Renderizar tipos como badges
                binding.layoutPokemonTypesDetail.removeAllViews()
                val context = binding.layoutPokemonTypesDetail.context
                for (type in it.types) {
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
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getBadgeDrawable(context: android.content.Context, colorRes: Int): android.graphics.drawable.Drawable {
        val radius = 32f
        val shape = android.graphics.drawable.GradientDrawable()
        shape.shape = android.graphics.drawable.GradientDrawable.RECTANGLE
        shape.cornerRadius = radius
        shape.setColor(context.getColor(colorRes))
        return shape
    }

    private fun getTypeColorRes(type: String): Int {
        return when (type) {
            "normal" -> com.example.demoapp.R.color.type_normal
            "fighting" -> com.example.demoapp.R.color.type_fighting
            "flying" -> com.example.demoapp.R.color.type_flying
            "poison" -> com.example.demoapp.R.color.type_poison
            "ground" -> com.example.demoapp.R.color.type_ground
            "rock" -> com.example.demoapp.R.color.type_rock
            "bug" -> com.example.demoapp.R.color.type_bug
            "ghost" -> com.example.demoapp.R.color.type_ghost
            "steel" -> com.example.demoapp.R.color.type_steel
            "fire" -> com.example.demoapp.R.color.type_fire
            "water" -> com.example.demoapp.R.color.type_water
            "grass" -> com.example.demoapp.R.color.type_grass
            "electric" -> com.example.demoapp.R.color.type_electric
            "psychic" -> com.example.demoapp.R.color.type_psychic
            "ice" -> com.example.demoapp.R.color.type_ice
            "dragon" -> com.example.demoapp.R.color.type_dragon
            "dark" -> com.example.demoapp.R.color.type_dark
            "fairy" -> com.example.demoapp.R.color.type_fairy
            else -> com.example.demoapp.R.color.blue_macropay
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
