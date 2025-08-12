package com.pokedex.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PokedexViewModel : ViewModel() {
    private val repository = com.pokedex.data.PokedexRepository()
    private val _pokemonList = MutableLiveData<List<Pokemon>>()
    val pokemonList: LiveData<List<Pokemon>> = _pokemonList
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private var offset = 0
    private val limit = 20
    private val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/"

    fun loadPokemons() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getPokemonList(offset, limit)
                val pokemons = response.results.mapIndexed { index, result ->
                    Pokemon(
                        id = offset + index + 1,
                        name = result.name.capitalize(),
                        //imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${offset + index + 1}.png",
                        imageUrl = "$imageUrl${offset + index + 1}.png",
                        types = emptyList() // Se llenará en el detalle
                    )
                }
                _pokemonList.value = pokemons
            } catch (e: Exception) {
                _pokemonList.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun nextPage() {
        offset += limit
        loadPokemons()
    }
    fun previousPage() {
        offset = maxOf(0, offset - limit)
        loadPokemons()
    }
}

// Modelo de datos básico
data class Pokemon(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val types: List<String>
)
