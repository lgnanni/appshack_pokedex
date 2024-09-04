package com.lgnanni.appshack.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.lgnanni.appshack.pokedex.repository.StoreData
import com.lgnanni.appshack.pokedex.ui.screens.DetailScreen
import com.lgnanni.appshack.pokedex.ui.screens.HomeScreen
import com.lgnanni.appshack.pokedex.ui.theme.AppshackPokedexTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val storeData = StoreData(this)
        enableEdgeToEdge()
        setContent {
            AppshackPokedexTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val pokemonCount = storeData.pokemonCountFlow.collectAsState(initial = 0)
                    val lastPokemon = storeData.lastPokemonFlow.collectAsState(initial = 0)

                    var randomPokemon = 0
                    if(pokemonCount.value > 0)
                        randomPokemon = (1.. pokemonCount.value).random()


                    if(randomPokemon > 0)
                        DetailScreen(modifier = Modifier.padding(innerPadding), randomPokemon)
                    else
                       HomeScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}