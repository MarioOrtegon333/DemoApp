package com.example.demoapp.domain.usecase

import com.example.demoapp.domain.model.PokemonDetail
import com.example.demoapp.domain.repository.PokedexRepository
import javax.inject.Inject

class GetPokemonDetailUseCase @Inject constructor(
    private val repository: PokedexRepository
) {
    suspend operator fun invoke(id: String): PokemonDetail {
        return repository.getPokemonDetail(id)
    }
}
