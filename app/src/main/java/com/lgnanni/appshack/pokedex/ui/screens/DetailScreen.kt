package com.lgnanni.appshack.pokedex.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.lgnanni.appshack.pokedex.viewmodel.PokemonDetailViewModel

@Composable
fun DetailScreen(modifier: Modifier, pokemonId: Int) {
    val vm: PokemonDetailViewModel = hiltViewModel()

}