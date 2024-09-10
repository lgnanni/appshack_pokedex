package com.lgnanni.appshack.pokedex.viewmodel

import androidx.lifecycle.ViewModel
import com.lgnanni.appshack.pokedex.model.PokemonListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


class MainViewModel: ViewModel() {

    val error = MutableStateFlow("")
    val connected = MutableStateFlow(false)
    val pokemonId = MutableStateFlow(0)
    val pokemons = MutableStateFlow(emptyList<PokemonListItem>())

    val navigateToDetails = MutableStateFlow(false)
    fun cleanError() { error.value = "" }
    fun setError(err: String) { error.value = err }

    fun setPokemonId(id: Int = 0) {
        pokemonId.value = id
    }


    fun setPokemons(pokemonsList: List<PokemonListItem>) { pokemons.value = pokemonsList }

    fun randomPokemon() {
        val id = (1..pokemons.value.size).random()
        setPokemonId(if (id == pokemonId.value)
            (1..pokemonId.value).random()
        else id)

        setNavToDetails(true)
    }

    fun setNavToDetails(nav: Boolean) {
        navigateToDetails.value = nav
    }

    fun firstLoad(): Boolean { return pokemons.value.isEmpty() }

    fun setIsConnected(conn: Boolean) {
        connected.value = conn
        if(conn) {
            cleanError()

        }
    }
}