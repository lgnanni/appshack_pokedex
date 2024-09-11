package com.lgnanni.appshack.pokedex.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lgnanni.appshack.pokedex.model.PokemonListItem
import com.lgnanni.appshack.pokedex.repository.PokedexRepoImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor (repository: PokedexRepoImpl): ViewModel() {
    val filteredList = MutableStateFlow(emptyList<PokemonListItem>())

    val pokemonListUiState = repository.getPokemonList()
        .map {
            list ->
            if (list.isNotEmpty())
                PokemonListUiState.ListPopulated(list)
            else
                PokemonListUiState.Error
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PokemonListUiState.Loading)

    fun setFilteredList(text: String  = "", starred: Boolean) {
        val initialList = if (starred) (pokemonListUiState.value as PokemonListUiState.ListPopulated).list.filter { it.starred }
        else (pokemonListUiState.value as PokemonListUiState.ListPopulated).list

        filteredList.value = initialList.filter{ it.name.contains(text) }
    }
}

sealed interface PokemonListUiState {
    data object Loading : PokemonListUiState
    data object Error : PokemonListUiState
    data class ListPopulated(val list: List<PokemonListItem>) : PokemonListUiState
}