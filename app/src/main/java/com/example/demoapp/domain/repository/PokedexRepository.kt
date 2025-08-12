package com.example.demoapp.domain.repository

import com.example.demoapp.domain.model.Pokemon
import com.example.demoapp.domain.model.PokemonDetail

interface PokedexRepository {
    suspend fun getPokemonList(offset: Int, limit: Int): List<Pokemon>
    suspend fun getPokemonDetail(id: String): PokemonDetail
}
