package com.lgnanni.appshack.pokedex.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel: ViewModel() {

    val error = MutableStateFlow("")
    val darkTheme = MutableStateFlow(false)
    val connected = MutableStateFlow(false)
    val pokemonId = MutableStateFlow(0)



    fun setDarkTheme(dark: Boolean) { darkTheme.value = dark }

    fun cleanError() { error.value = "" }
    fun setError(err: String) { error.value = err }

    fun setPokemonId(id: Int = 0) { pokemonId.value = id }

    fun setIsConnected(conn: Boolean) {
        connected.value = conn
        if(conn) {
            cleanError()

        }
    }
}