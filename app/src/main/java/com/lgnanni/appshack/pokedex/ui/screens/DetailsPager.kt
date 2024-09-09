package com.lgnanni.appshack.pokedex.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.lgnanni.appshack.pokedex.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DetailsPager(vm: MainViewModel) {

    val pokeCount by vm.pokemonCount.collectAsStateWithLifecycle()

    val pokemonId by vm.pokemonId.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState(pageCount = { pokeCount })


    HorizontalPager(state = pagerState) {
        DetailScreen(pagerState.currentPage)
    }

    LaunchedEffect(key1 = pokemonId) {
        if (pagerState.currentPage != pokemonId)
            vm.viewModelScope.launch { pagerState.scrollToPage(pokemonId) }
    }

}