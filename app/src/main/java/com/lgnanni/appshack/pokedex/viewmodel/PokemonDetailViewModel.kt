package com.lgnanni.appshack.pokedex.viewmodel

import androidx.lifecycle.ViewModel
import com.lgnanni.appshack.pokedex.repository.PokedexRepoImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor (repository: PokedexRepoImpl): ViewModel() {

}
