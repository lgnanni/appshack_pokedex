package com.lgnanni.appshack.pokedex.repository

import com.lgnanni.appshack.pokedex.model.PokemonData

interface RemotePokemonData {
    fun getRemoteData() : PokemonData
}

class RemotePokemonDataImpl: RemotePokemonData {
    override fun getRemoteData(): PokemonData {
        TODO("Not yet implemented")
    }

}