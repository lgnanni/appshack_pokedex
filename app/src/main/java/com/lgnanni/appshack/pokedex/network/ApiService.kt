package com.lgnanni.appshack.pokedex.network

import com.lgnanni.appshack.pokedex.model.Pokemon
import com.lgnanni.appshack.pokedex.model.PokemonList
import com.lgnanni.appshack.pokedex.model.SpeciesInfo
import com.lgnanni.appshack.pokedex.model.PokemonType
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET(RetrofitInstance.BASE_URL + "pokemon")
    suspend fun getPokemons(): Response<PokemonList>

    @GET("{path}")
    suspend fun getPokemonDetails(@Path("path") path: String): Response<Pokemon>

    @GET("{path}")
    suspend fun getSpeciesDetails(@Path("path") path: String): Response<SpeciesInfo>

    @GET("{path}")
    suspend fun getTypeDetails(@Path("path") path: String): Response<PokemonType>

    @GET("{path}")
    suspend fun fetchPokemonSpecies(@Path("path") path: String): Response<SpeciesInfo>

}