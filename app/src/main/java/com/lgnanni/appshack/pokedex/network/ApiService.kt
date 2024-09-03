package com.lgnanni.appshack.pokedex.network

import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET()
    suspend fun getPokemons(): Response<Any>

    @GET()
    suspend fun getPokemonDetails(): Response<Any>

    @GET()
    suspend fun getSpeciesDetails(): Response<Any>

    @GET()
    suspend fun getTypeDetails(): Response<Any>
}