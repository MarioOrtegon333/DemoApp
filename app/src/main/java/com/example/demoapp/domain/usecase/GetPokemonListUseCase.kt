package com.example.demoapp.domain.usecase

import com.example.demoapp.domain.model.Pokemon
import com.example.demoapp.domain.repository.PokedexRepository
import javax.inject.Inject

class GetPokemonListUseCase @Inject constructor(
    private val repository: PokedexRepository
) {
    suspend operator fun invoke(offset: Int, limit: Int): List<Pokemon> {
        return repository.getPokemonList(offset, limit)
    }
}
