package com.lgnanni.appshack.pokedex.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel: ViewModel() {

    val error = MutableStateFlow("")
    val darkTheme = MutableStateFlow(false)
    val connected = MutableStateFlow(false)
    val pokemonId = MutableStateFlow(0)
    val lastPokemonId = MutableStateFlow(0)
    private val pokemonCount = MutableStateFlow(0)

    fun setDarkTheme(dark: Boolean) { darkTheme.value = dark }

    fun cleanError() { error.value = "" }
    fun setError(err: String) { error.value = err }

    fun setPokemonId(id: Int = 0) {
        lastPokemonId.value = pokemonId.value
        pokemonId.value = id
    }
    fun setPokemonCount(count: Int) { pokemonCount.value = count }

    fun randomPokemon() {
        val id = (1..pokemonCount.value).random()
        pokemonId.value = if (id == lastPokemonId.value) (1..lastPokemonId.value).random() else id
    }

    fun setIsConnected(conn: Boolean) {
        connected.value = conn
        if(conn) {
            cleanError()

        }
    }
}