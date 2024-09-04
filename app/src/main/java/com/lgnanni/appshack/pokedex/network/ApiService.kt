package com.lgnanni.appshack.pokedex.network

import com.lgnanni.appshack.pokedex.model.Pokemon
import com.lgnanni.appshack.pokedex.model.PokemonList
import com.lgnanni.appshack.pokedex.model.SpeciesInfo
import com.lgnanni.appshack.pokedex.model.PokemonType
import com.lgnanni.appshack.pokedex.model.TypeSprite
import com.lgnanni.appshack.pokedex.model.TypeSpriteInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("pokemon")
    suspend fun getPokemons(): Response<PokemonList>

    @GET("pokemon")
    suspend fun getPokemonsOffset(@Query("offset") offset: Int, @Query("limit") limit: Int): Response<PokemonList>

    @GET("pokemon/{id}")
    suspend fun getPokemonDetails(@Path("id") id: Int): Response<Pokemon>

    @GET("{path}")
    suspend fun getSpeciesDetails(@Path("path") path: String): Response<SpeciesInfo>

    @GET("type/{path}")
    suspend fun getTypeSprite(@Path("path") path: String): Response<TypeSpriteInfo>
}
