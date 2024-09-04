package com.lgnanni.appshack.pokedex.network

import com.lgnanni.appshack.pokedex.model.Pokemon
import com.lgnanni.appshack.pokedex.model.PokemonList
import com.lgnanni.appshack.pokedex.model.SpeciesInfo
import com.lgnanni.appshack.pokedex.model.PokemonType
import com.lgnanni.appshack.pokedex.model.TypeSprite
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("pokemon")
    suspend fun getPokemons(): Response<PokemonList>

    @GET("{path}")
    suspend fun getPokemonsOffset(@Path("path") path: String): Response<PokemonList>

    @GET("pokemon/{id}")
    suspend fun getPokemonDetails(@Path("id") id: Int): Response<Pokemon>

    @GET("{path}")
    suspend fun getSpeciesDetails(@Path("path") path: String): Response<SpeciesInfo>

    @GET("{path}")
    suspend fun getTypeSprite(@Path("path") path: String): Response<TypeSprite>

    fun removeBaseUrl(url: String) : String {
        return url.removePrefix(RetrofitModule.BASE_URL)
    }
}
