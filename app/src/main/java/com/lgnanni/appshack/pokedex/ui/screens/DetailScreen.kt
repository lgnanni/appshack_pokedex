package com.lgnanni.appshack.pokedex.ui.screens

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.lgnanni.appshack.pokedex.R
import com.lgnanni.appshack.pokedex.model.PokemonDetails
import com.lgnanni.appshack.pokedex.model.SpeciesData
import com.lgnanni.appshack.pokedex.viewmodel.MainViewModel
import com.lgnanni.appshack.pokedex.viewmodel.PokemonDetailViewModel
import com.lgnanni.appshack.pokedex.viewmodel.UiState
import kotlinx.coroutines.coroutineScope
import okio.IOException

@Composable
fun DetailScreen(pokeId: Int) {
    val vm: PokemonDetailViewModel = hiltViewModel()

    val uiState by vm.uiState.collectAsStateWithLifecycle(UiState.Loading)
    val firstLoad by vm.firstLoad.collectAsStateWithLifecycle()
    val vmPokeId by vm.pokemonId.collectAsStateWithLifecycle()

    if (pokeId != vmPokeId) {
        vm.fetchNewPokemon(pokeId)
        vm.setFirstLoad(true)
    }

    when(uiState) {
        is UiState.Error -> {}
        is UiState.Success -> {
            val details = (uiState as UiState.Success<PokemonDetails>).data

            if (firstLoad && details.id == pokeId) {
                playCry(details.cries.latest)
                vm.setFirstLoad(false)
            }

            PokemonDetailsView(vm, details)
        }
        is UiState.Loading -> { LoadingScreen() }

        is UiState.Empty -> {}
    }
}

@Composable
fun LoadingScreen () {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(modifier = Modifier
            .fillMaxWidth(0.25f)
            .align(Alignment.Center))
    }
}

@Composable
fun PokemonDetailsView(vm: PokemonDetailViewModel, details: PokemonDetails) {
    var shiny by remember { mutableStateOf(false) }

    val imageLoaderDefault = ImageRequest.Builder(LocalContext.current)
        .data(details.officialSprite.frontDefault)
        .crossfade(true)
        .build()

    val imageLoaderShiny = ImageRequest.Builder(LocalContext.current)
        .data(details.officialSprite.frontShiny)
        .crossfade(true)
        .build()

    val typeSpritesBuilders = emptyList<ImageRequest>().toMutableList()

    details.typeSprites.forEach {
        val imageType = ImageRequest.Builder(LocalContext.current)
            .data(it)
            .crossfade(true)
            .build()

        typeSpritesBuilders.add(imageType)
    }

    Column(modifier = Modifier.verticalScroll(rememberScrollState()).padding(8.dp)) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
            this@Column.AnimatedVisibility(
                visible = !shiny,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                SubcomposeAsyncImage(
                    modifier = Modifier
                        .clickable { shiny = true }
                        .fillMaxWidth()
                        .align(Alignment.TopStart),
                    model = imageLoaderDefault,
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    loading =
                    {
                        Box(
                            modifier = Modifier
                                .height(300.dp)
                                .fillMaxWidth()
                                .padding(20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                )
            }

            this@Column.AnimatedVisibility(
                visible = shiny,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                SubcomposeAsyncImage(
                    modifier = Modifier
                        .clickable { shiny = false }
                        .fillMaxWidth()
                        .align(Alignment.TopStart),
                    model = imageLoaderShiny,
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    loading =
                    {
                        Box(
                            modifier = Modifier
                                .height(300.dp)
                                .fillMaxWidth()
                                .padding(20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                )
            }
            val starIconBoolean = remember { mutableStateOf(details.starred) }

            IconToggleButton(
                checked = starIconBoolean.value, onCheckedChange = {
                    vm.updatePokemon(details.name, it)
                    starIconBoolean.value = it
                }, modifier = Modifier.align(Alignment.TopEnd)) {

                val starIcon = if(starIconBoolean.value) Icons.Filled.Star else Icons.Filled.StarBorder
                Icon(imageVector = starIcon, contentDescription = stringResource(id = R.string.starred))
            }

        }
        Row (verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "#${details.id} ${details.name.capitalize(Locale.current)}",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.width(6.dp))

                IconButton(onClick = {
                    playCry(details.cries.latest)
                }) {
                    Icon(
                        imageVector = Icons.Filled.PlayCircle,
                        contentDescription = null
                    )
                }
        }

        Row (modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)){
            typeSpritesBuilders.forEach {
                SubcomposeAsyncImage(
                    model = it,
                    contentDescription = null)
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        val text =
            details.speciesData.flavorTexts.first { it.language.name.contentEquals("en") }

        Text(
            text = text.flavorText,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Start)

        if (details.speciesData.evolvesTo.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            EvolveData(speciesData = details.speciesData)
        } else {
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = stringResource(id = R.string.finalForm),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start)
        }
    }

}

@Composable
private fun EvolveData(speciesData: SpeciesData) {
    //As the information to get evolution methods is quite convoluted to access
    //will show minimal info, on a future iteration could improve and make it nicer

    Column {
        Text(text = String.format(stringResource(id = R.string.evolvesTo),
            speciesData.evolvesTo.capitalize(Locale.current)),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start)

        Text(text = String.format(stringResource(id = R.string.method),
            speciesData.evolutionTrigger.names.find { it.language.name == "en" }!!.name),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start)
    }

}

private fun playCry(url: String) {
    val mediaPlayer = MediaPlayer()

    mediaPlayer.setAudioAttributes(
        AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build())

    try {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepare()
        mediaPlayer.setOnPreparedListener {

            it.start()
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }

}