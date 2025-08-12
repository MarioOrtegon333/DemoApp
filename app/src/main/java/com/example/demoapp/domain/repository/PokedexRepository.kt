package com.example.demoapp.domain.repository

import com.example.demoapp.data.RetrofitInstance
import com.example.demoapp.data.PokemonListResponse
import com.example.demoapp.data.PokemonDetailResponse

class PokedexRepository {
    suspend fun getPokemonList(offset: Int = 0, limit: Int = 20): PokemonListResponse {
        return RetrofitInstance.api.getPokemonList(offset, limit)
    }

    suspend fun getPokemonDetail(id: String): PokemonDetailResponse {
        return RetrofitInstance.api.getPokemonDetail(id)
    }
}
