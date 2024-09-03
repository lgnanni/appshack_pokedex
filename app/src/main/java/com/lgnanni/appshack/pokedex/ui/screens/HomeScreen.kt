package com.lgnanni.appshack.pokedex.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.lgnanni.appshack.pokedex.viewmodel.PokemonListViewModel

@Composable
fun HomeScreen(modifier: Modifier) {
    val vm: PokemonListViewModel = hiltViewModel()

    val result = vm.pokemonList.collectAsState()
}