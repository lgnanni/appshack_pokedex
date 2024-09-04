package com.lgnanni.appshack.pokedex.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoreData(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    }

    private val POKEMON_COUNT = intPreferencesKey("pokemon_count")
    private val NEXT_PAGE = stringPreferencesKey("next_page")
    private val LAST_POKEMON = intPreferencesKey("last_pokemon")


    val pokemonCountFlow: Flow<Int> = context.dataStore.data.map {
        preferences -> preferences[POKEMON_COUNT] ?: 0
    }


    val lastPokemonFlow: Flow<Int> = context.dataStore.data.map {
            preferences -> preferences[LAST_POKEMON] ?: 0
    }

    val nextPageFlow: Flow<String> = context.dataStore.data.map {
        preferences -> preferences[NEXT_PAGE] ?: ""
    }


    suspend fun setNextPage(nextPage: String) {
        context.dataStore.edit {
                settings -> settings[NEXT_PAGE] = nextPage
        }
    }

    suspend fun setLastPokemon(lastPokemon: Int) {
        context.dataStore.edit {
                settings -> settings[LAST_POKEMON] = lastPokemon
        }
    }

    suspend fun setPokemonCount(count: Int) {
        context.dataStore.edit {
                settings -> settings[POKEMON_COUNT] = count
        }
    }
}