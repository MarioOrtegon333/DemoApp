package com.pokedex.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pokedex.data.models.StatsItem
import kotlinx.coroutines.launch

class PokemonDetailViewModel : ViewModel() {
    private val repository = com.pokedex.data.PokedexRepository()
    private val _pokemonDetail = MutableLiveData<PokemonDetail>()
    val pokemonDetail: LiveData<PokemonDetail> = _pokemonDetail
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun loadPokemonDetail(id: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getPokemonDetail(id)
                val artworkUrl = response.sprites.other?.official_artwork?.front_default ?: response.sprites.front_default ?: ""
                val detail = PokemonDetail(
                    id = response.id,
                    name = response.name.capitalize(),
                    imageUrl = artworkUrl,
                    types = response.types.map { it.type.name.capitalize() },
                    height = response.height,
                    weight = response.weight,
                    abilities = response.abilities.map { it.ability.name.capitalize() },
                    stats = response.stats

                )
                _pokemonDetail.value = detail
            } catch (e: Exception) {
                _pokemonDetail.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }
}

// Modelo de datos para el detalle
data class PokemonDetail(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val types: List<String>,
    val height: Int,
    val weight: Int,
    val abilities: List<String>,
    val stats: List<StatsItem> = emptyList()
)
