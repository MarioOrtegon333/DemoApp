package com.example.demoapp.presentation.ui.pokemonlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.demoapp.domain.usecase.GetPokemonListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class PokedexViewModel @Inject constructor(
    private val getPokemonListUseCase: GetPokemonListUseCase
) : ViewModel() {
    private val _pokemonList = MutableLiveData<List<com.example.demoapp.domain.model.Pokemon>?>(null)
    val pokemonList: LiveData<List<com.example.demoapp.domain.model.Pokemon>?> = _pokemonList
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private var offset = 0
    private val limit = 20

    fun loadPokemons() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val pokemons = getPokemonListUseCase(offset, limit)
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