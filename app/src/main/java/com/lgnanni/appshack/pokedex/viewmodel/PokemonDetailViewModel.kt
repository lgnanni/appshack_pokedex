package com.lgnanni.appshack.pokedex.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lgnanni.appshack.pokedex.model.PokemonDetails
import com.lgnanni.appshack.pokedex.model.PokemonListItem
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
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor (
    repository: PokedexRepoImpl,
    savedStateHandle: SavedStateHandle, ): ViewModel() {


    private val _pokemonId = MutableStateFlow(savedStateHandle["pokemonId"] ?: 0) // Default value if not provided
    val pokemonId: StateFlow<Int> = _pokemonId.asStateFlow()

    val firstLoad = MutableStateFlow(true)

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState = _pokemonId.flatMapLatest {
        repository.getPokemonDetails(_pokemonId.value)
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


fun <T, R> UiState<T>.mapSuccess(transform: (T) -> R): UiState<R> {
    return when (this) {
        is UiState.Loading -> UiState.Loading
        is UiState.Success -> UiState.Success(transform(data))
        is UiState.Error -> UiState.Error(message)
        is UiState.Empty -> UiState.Empty(message)
    }
}


        /*
sealed interface PokemonDetailsUiState {
    data object Loading : PokemonDetailsUiState
    data object Error : PokemonDetailsUiState
    data class DetailsLoaded(val details: PokemonDetails) : PokemonDetailsUiState
}
*/