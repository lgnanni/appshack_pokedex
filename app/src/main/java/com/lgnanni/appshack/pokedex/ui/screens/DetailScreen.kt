package com.lgnanni.appshack.pokedex.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lgnanni.appshack.pokedex.viewmodel.PokemonDetailViewModel

@Composable
fun DetailScreen() {
    val vm: PokemonDetailViewModel = hiltViewModel()

    val uiState by vm.pokemonDetailsUiState.collectAsStateWithLifecycle()

}