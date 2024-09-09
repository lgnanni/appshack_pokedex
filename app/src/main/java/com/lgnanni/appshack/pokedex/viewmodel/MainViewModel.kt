package com.lgnanni.appshack.pokedex.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


class MainViewModel: ViewModel() {

    val error = MutableStateFlow("")
    val darkTheme = MutableStateFlow(false)
    val connected = MutableStateFlow(false)
    val pokemonId = MutableStateFlow(0)
    val lastPokemonId = MutableStateFlow(0)
    val pokemonCount = MutableStateFlow(0)

    fun setDarkTheme(dark: Boolean) { darkTheme.value = dark }

    fun cleanError() { error.value = "" }
    fun setError(err: String) { error.value = err }

    fun setPokemonId(id: Int = 0) {
        setLastPokemonId()
        pokemonId.value = id
    }

    fun setLastPokemonId() { lastPokemonId.value = pokemonId.value }

    fun setPokemonCount(count: Int) { pokemonCount.value = count }

    fun randomPokemon() {
        val id = (1..pokemonCount.value).random()
        setPokemonId(if (id == lastPokemonId.value) (1..lastPokemonId.value).random() else id)
    }

    fun firstLoad(): Boolean { return pokemonCount.value == 0 }

    fun setIsConnected(conn: Boolean) {
        connected.value = conn
        if(conn) {
            cleanError()

        }
    }
}