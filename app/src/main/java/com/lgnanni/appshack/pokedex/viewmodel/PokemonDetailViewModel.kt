package com.lgnanni.appshack.pokedex.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lgnanni.appshack.pokedex.repository.PokedexRepoImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor (
    repository: PokedexRepoImpl,
    savedStateHandle: SavedStateHandle, ): ViewModel() {


    private val repo = repository

    private val _pokemonId = MutableStateFlow(savedStateHandle["pokemonId"] ?: 0) // Default value if not provided
    val pokemonId: StateFlow<Int> = _pokemonId.asStateFlow()

    val firstLoad = MutableStateFlow(true)

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState = _pokemonId.flatMapLatest {
        repo.getPokemonDetails(_pokemonId.value)
            .asUiState()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                UiState.Loading)
    }

    fun fetchNewPokemon(pokeId: Int) {
        _pokemonId.value = pokeId
    }


    fun setFirstLoad(value: Boolean) {
        firstLoad.value = value
    }

    fun updatePokemon(name: String, starred: Boolean) {
        viewModelScope.launch {
            repo.updatePokemon(name, starred)
        }
    }

}

sealed interface UiState<out T> {
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String?) : UiState<Nothing>
    data object Loading : UiState<Nothing>
    data class Empty(val message: String?) : UiState<Nothing>
}

fun <T> Flow<T>.asUiState(
    errorMessage: String? = null
): Flow<UiState<T>> =
    map<T, UiState<T>> {
        UiState.Success(it)
    }.catch {
        emit(UiState.Error(errorMessage ?: it.message))
    }