package com.example.demoapp.presentation.ui.pokemonlist.adapter

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.demoapp.databinding.ItemPokemonBinding
import com.example.demoapp.domain.model.Pokemon
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
    }

    override fun getItemCount(): Int = items.size
}
