package com.lgnanni.appshack.pokedex.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lgnanni.appshack.pokedex.viewmodel.PokemonListUiState
import com.lgnanni.appshack.pokedex.viewmodel.PokemonListViewModel

@Composable
fun HomeScreen() {
    val vm: PokemonListViewModel = hiltViewModel()

    val uiState by vm.pokemonListUiState.collectAsStateWithLifecycle()

    when(uiState) {
        is PokemonListUiState.Loading -> {}
        is PokemonListUiState.ListPopulated -> {
            val listPopulated = (uiState as PokemonListUiState.ListPopulated)
            LazyColumn {
                items(listPopulated.list.size) { pokemonIndex ->
                    HomeItem(pokemonIndex + 1,listPopulated.list[pokemonIndex].name) {
                        vm.setSelectedId(pokemonIndex)
                    }
                }
            }
        }
        is PokemonListUiState.Error -> {}
    }



}