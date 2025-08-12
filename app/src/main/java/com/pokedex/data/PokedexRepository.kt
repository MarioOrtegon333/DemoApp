package com.pokedex.data

import com.pokedex.data.RetrofitInstance
import com.pokedex.data.PokemonListResponse
import com.pokedex.data.PokemonDetailResponse

class PokedexRepository {
    suspend fun getPokemonList(offset: Int = 0, limit: Int = 20): PokemonListResponse {
        return RetrofitInstance.api.getPokemonList(offset, limit)
    }

    suspend fun getPokemonDetail(id: String): PokemonDetailResponse {
        return RetrofitInstance.api.getPokemonDetail(id)
    }
}
