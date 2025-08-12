package com.pokedex.ui.adapter

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.demoapp.databinding.ItemPokemonBinding
import com.pokedex.ui.Pokemon
import com.squareup.picasso.Picasso


class PokemonAdapter(
    private val items: List<Pokemon>,
    private val onItemClick: (Pokemon) -> Unit
) : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

    inner class PokemonViewHolder(val binding: ItemPokemonBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val binding = ItemPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PokemonViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = items[position]
        holder.binding.tvPokemonName.text = pokemon.name
        holder.binding.tvPokemonNumber.text = "#${pokemon.id.toString().padStart(3, '0')}"
        Picasso.get().load(pokemon.imageUrl).into(holder.binding.ivPokemonImage)
        holder.binding.root.setOnClickListener { onItemClick(pokemon) }

        // Limpiar tipos previos
        holder.binding.layoutPokemonTypes.removeAllViews()
        val context = holder.binding.layoutPokemonTypes.context
        for (type in pokemon.types) {
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
            holder.binding.layoutPokemonTypes.addView(badge)
        }
        Log.d("PokemonAdapter", "Pokemon types of ${pokemon.name}:  ${pokemon.types}")
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

    override fun getItemCount(): Int = items.size
}
