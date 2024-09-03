package com.lgnanni.appshack.pokedex.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lgnanni.appshack.pokedex.model.PokemonListItem
import com.lgnanni.appshack.pokedex.repository.PokedexRepoImpl
import dagger.hilt.android.lifecycle.HiltViewModel


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor (repository: PokedexRepoImpl): ViewModel() {

    val pokemonList: StateFlow<List<PokemonListItem>> = repository.getPokemonList()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}

    /*
    var apiService = RetrofitInstance.api

    val countries = MutableStateFlow(emptyList<Country>())
    val filteredCountries = MutableStateFlow(emptyList<Country>())
    val loading = MutableStateFlow(false)
    val error = MutableStateFlow("")
    val darkTheme = MutableStateFlow(false)
    val connected = MutableStateFlow(false)
    val regions = MutableStateFlow(emptyList<String>().toMutableList())
    private val searchCountry = MutableStateFlow("")
    val selectedCountry = MutableStateFlow(Country())
    val currentRegion = MutableStateFlow("Worldwide")

    private var job: Job? = null

    //Given the API not providing pagination features, we have to receive all the data at once
    //in a different scenario we could implement that and so fetch the data in small chunks for
    //a more agile experience
    fun getCountries() {
        if(!loading.value && connected.value) {
            loading.value = true
            cleanError()

            job = CoroutineScope(Dispatchers.IO).launch {
                val response = apiService.getCountries()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        countries.value = response.body()!!
                        getRegions()
                        cleanError()
                        setSearchCountry("")
                    } else {
                        loading.value = false
                        error.value = response.message()
                    }
                }
            }
        }
    }
    fun setDarkTheme(dark: Boolean) { darkTheme.value = dark }

    fun cleanError() { error.value = "" }

    fun getRegions() {
        regions.value.add("Worldwide")
        for (country in countries.value) {
            if (!regions.value.contains(country.region))
                regions.value.add(country.region)
        }
    }

    fun setError(err: String) { error.value = err }

    fun setIsConnected(conn: Boolean) {
        connected.value = conn
        if(conn && countries.value.isEmpty()) {
            cleanError()
            getCountries()
        }
    }

    fun clearCountry() {
        selectedCountry.value = Country()
    }

    fun filterCountriesByRegion(text: String) {
        if(text.contentEquals(regions.value.first()))
            filteredCountries.value = countries.value.sortedWith(compareBy { it.name.common.lowercase() }).filter { country -> country.name.common.contains(text, ignoreCase = true) }
        else
            filteredCountries.value = countries.value.sortedWith(compareBy { it.name.common.lowercase() }).filter { country -> country.region.contentEquals(text, ignoreCase = true) }

        currentRegion.value = text
    }
    fun setSearchCountry(text: String) {
        searchCountry.value = text

        //If we don't do the filtering by region we will be able to find any country regardless of what the menu says
        if(currentRegion.value.contentEquals(regions.value.first())) {
            filteredCountries.value =
                countries.value.sortedWith(compareBy { it.name.common.lowercase() })
                    .filter { country -> country.name.common.contains(text, ignoreCase = true) }
        }
        else {
            filteredCountries.value =
                countries.value.sortedWith(compareBy { it.name.common.lowercase() })
                    .filter { country -> country.region.contentEquals(currentRegion.value, ignoreCase = true) }
                    .filter { country -> country.name.common.contains(text, ignoreCase = true) }
        }

        loading.value = false
    }

     */