package com.lgnanni.appshack.pokedex.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lgnanni.appshack.pokedex.ui.screens.views.SearchField
import com.lgnanni.appshack.pokedex.viewmodel.MainViewModel
import com.lgnanni.appshack.pokedex.viewmodel.PokemonListUiState
import com.lgnanni.appshack.pokedex.viewmodel.PokemonListViewModel

@Composable
fun HomeScreen(mainVm: MainViewModel) {
    val vm: PokemonListViewModel = hiltViewModel()

    val uiState by vm.pokemonListUiState.collectAsStateWithLifecycle()
    val filteredList by vm.filteredList.collectAsStateWithLifecycle()

    when(uiState) {
        is PokemonListUiState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(modifier = Modifier
                    .fillMaxWidth(0.25f)
                    .align(Alignment.Center))
            }
        }
        is PokemonListUiState.ListPopulated -> {
            var text by remember { mutableStateOf("") }
            vm.setFilteredList()

            Column {
                SearchField {
                    text = it
                    vm.setFilteredList(it)
                }
                LazyColumn {
                    items(filteredList.size) { pokemonIndex ->
                        HomeItem(pokemonIndex + 1, filteredList[pokemonIndex].name) {
                            mainVm.setPokemonId(pokemonIndex + 1)
                        }
                    }
                }
            }
        }
        is PokemonListUiState.Error -> {}
    }



}