package com.example.demoapp.presentation.ui.pokemondetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.demoapp.domain.usecase.GetPokemonDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.example.demoapp.domain.model.PokemonDetail
import kotlinx.coroutines.launch

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val getPokemonDetailUseCase: GetPokemonDetailUseCase
) : ViewModel() {
    private val _pokemonDetail = MutableLiveData<PokemonDetail?>()
    val pokemonDetail: LiveData<PokemonDetail?> = _pokemonDetail
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun loadPokemonDetail(id: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val detail = getPokemonDetailUseCase(id)
                _pokemonDetail.value = detail
            } catch (e: Exception) {
                _pokemonDetail.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }
}