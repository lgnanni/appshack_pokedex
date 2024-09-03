package com.lgnanni.appshack.pokedex.repository

import com.lgnanni.appshack.pokedex.model.PokemonData

interface LocalPokemonData {

    fun getLocalData(): PokemonData
}

class LocalPokemonDataImpl: LocalPokemonData {
    override fun getLocalData(): PokemonData {
        TODO("Not yet implemented")
    }

}