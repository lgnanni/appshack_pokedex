package com.lgnanni.appshack.pokedex.ui.screens

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.lgnanni.appshack.pokedex.model.PokemonDetails
import com.lgnanni.appshack.pokedex.viewmodel.PokemonDetailViewModel
import com.lgnanni.appshack.pokedex.viewmodel.PokemonDetailsUiState
import com.lgnanni.appshack.pokedex.viewmodel.PokemonListUiState
import okio.IOException

@Composable
fun DetailScreen() {
    val vm: PokemonDetailViewModel = hiltViewModel()

    val uiState by vm.pokemonDetailsUiState.collectAsStateWithLifecycle()
    when(uiState) {
        is PokemonDetailsUiState.Error -> {}
        is PokemonDetailsUiState.DetailsLoaded -> {
            val details = (uiState as PokemonDetailsUiState.DetailsLoaded).details
            PokemonDetailsView(details = details)
            playCry(details.cries.latest)

        }
        is PokemonDetailsUiState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(modifier = Modifier.fillMaxWidth(0.25f).align(Alignment.Center))
            }
        }
    }
}

@Composable
fun PokemonDetailsView(details: PokemonDetails) {
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

    Column(modifier = Modifier.padding(8.dp)) {
        Row (modifier = Modifier
            .height(160.dp)
            .fillMaxWidth()) {

            Box {
                this@Row.AnimatedVisibility(
                    visible = !shiny,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    SubcomposeAsyncImage(
                        modifier = Modifier.clickable { shiny = true },
                        model = imageLoaderDefault,
                        contentDescription = null,
                        contentScale = ContentScale.FillHeight,
                        loading =
                        {
                            Box(
                                modifier = Modifier
                                    .height(100.dp)
                                    .padding(20.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    )
                }

                this@Row.AnimatedVisibility(
                    visible = shiny,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    SubcomposeAsyncImage(
                        modifier = Modifier.clickable { shiny = false },
                        model = imageLoaderShiny,
                        contentDescription = null,
                        contentScale = ContentScale.FillHeight,
                        loading =
                        {
                            Box(
                                modifier = Modifier
                                    .height(100.dp)
                                    .padding(20.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    )
                }

            }

            Column(modifier = Modifier.height(160.dp)) {
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

                Spacer(modifier = Modifier.weight(1f))

                Row  {
                    typeSpritesBuilders.forEach {
                        SubcomposeAsyncImage(
                            model = it,
                            contentDescription = null,
                            contentScale = ContentScale.FillHeight)
                    }
                }
            }
        }

        val text = details.speciesInfo.flavorTextEntries.filter { it.language.name.contentEquals("en") }.first()

        Text(
            text = text.flavorText,
            style = MaterialTheme.typography.bodyLarge)
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
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            it.start()
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }

}