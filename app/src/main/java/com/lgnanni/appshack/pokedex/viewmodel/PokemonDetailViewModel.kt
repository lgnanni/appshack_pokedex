package com.lgnanni.appshack.pokedex.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lgnanni.appshack.pokedex.model.PokemonDetails
import com.lgnanni.appshack.pokedex.model.PokemonListItem
import com.lgnanni.appshack.pokedex.repository.PokedexRepoImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor (
    repository: PokedexRepoImpl,
    savedStateHandle: SavedStateHandle): ViewModel() {

    private val pokemonId: Int = savedStateHandle["pokemonId"] ?: 0 // Default value if not provided

    val firstLoad = MutableStateFlow(true)

    val pokemonDetailsUiState = repository.getPokemonDetails(pokemonId)
        .map {
                details ->
            if (details.name.isNotEmpty())
                PokemonDetailsUiState.DetailsLoaded(details)
            else
                PokemonDetailsUiState.Error
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PokemonDetailsUiState.Loading)

    fun flagFirstLoad() {
        firstLoad.value = false
    }
}

sealed interface PokemonDetailsUiState {
    data object Loading : PokemonDetailsUiState
    data object Error : PokemonDetailsUiState
    data class DetailsLoaded(val details: PokemonDetails) : PokemonDetailsUiState
}
