package com.example.demoapp.data.repository

import com.example.demoapp.data.RetrofitInstance
import com.example.demoapp.domain.model.Pokemon
import com.example.demoapp.domain.model.PokemonDetail
import com.example.demoapp.domain.model.Stat
import com.example.demoapp.domain.repository.PokedexRepository

class PokedexRepositoryImpl : PokedexRepository {
    override suspend fun getPokemonList(offset: Int, limit: Int): List<Pokemon> {
        val response = RetrofitInstance.api.getPokemonList(offset, limit)
        return response.results.mapIndexed { index, result ->
            Pokemon(
                id = offset + index + 1,
                name = result.name.capitalize(),
                imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${offset + index + 1}.png",
                types = emptyList()
            )
        }
    }

    override suspend fun getPokemonDetail(id: String): PokemonDetail {
        val response = RetrofitInstance.api.getPokemonDetail(id)
        val artworkUrl = response.sprites.other?.official_artwork?.front_default ?: response.sprites.front_default ?: ""
        return PokemonDetail(
            id = response.id,
            name = response.name.capitalize(),
            imageUrl = artworkUrl,
            types = response.types.map { it.type.name.capitalize() },
            height = response.height,
            weight = response.weight,
            abilities = response.abilities.map { it.ability.name.capitalize() },
            stats = response.stats.map { Stat(
                name = it.stat.name,
                baseStat = it.base_stat
            ) }
        )
    }
}
