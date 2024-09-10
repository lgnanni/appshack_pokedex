package com.lgnanni.appshack.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lgnanni.appshack.pokedex.network.ConnectionState
import com.lgnanni.appshack.pokedex.network.connectivityState
import com.lgnanni.appshack.pokedex.ui.screens.BottomNavigationBar
import com.lgnanni.appshack.pokedex.ui.screens.DetailScreen
import com.lgnanni.appshack.pokedex.ui.screens.DetailsPager
import com.lgnanni.appshack.pokedex.ui.screens.HomeScreen
import com.lgnanni.appshack.pokedex.ui.theme.AppshackPokedexTheme
import com.lgnanni.appshack.pokedex.viewmodel.MainViewModel
import com.lgnanni.appshack.pokedex.viewmodel.PokemonListUiState
import com.lgnanni.appshack.pokedex.viewmodel.PokemonListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val vm: MainViewModel by viewModels()
    private val vmList: PokemonListViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val darkTheme = remember { mutableStateOf(false) }
            val error: String by vm.error.collectAsStateWithLifecycle("")

            val pokemonId by vm.pokemonId.collectAsStateWithLifecycle()
            val navToDetails by vm.navigateToDetails.collectAsStateWithLifecycle()

            val snackBarHostState = remember { SnackbarHostState() }
            val navController = rememberNavController()

            var canPop by remember { mutableStateOf(false) }

            val pokemonListUiState: PokemonListUiState by vmList.pokemonListUiState.collectAsStateWithLifecycle()

            when(pokemonListUiState) {
                is PokemonListUiState.Loading -> {}
                is PokemonListUiState.ListPopulated -> {
                    val listPopulated = (pokemonListUiState as PokemonListUiState.ListPopulated)
                    if(vm.firstLoad()) {
                        vm.setPokemons(listPopulated.list)
                        vm.randomPokemon()
                    }
                }
                is PokemonListUiState.Error -> {}
            }

            //Listen to have visual feedback of navigation returns
            DisposableEffect(navController) {
                val listener = NavController.OnDestinationChangedListener { controller, _, _ ->
                    canPop = controller.previousBackStackEntry != null
                }
                navController.addOnDestinationChangedListener(listener)
                onDispose {
                    navController.removeOnDestinationChangedListener(listener)
                }
            }

            //Custom back implementation to handle navigation properly
            BackHandler {
                if (navController.currentDestination?.route.contentEquals("home"))
                    this.finish()
            }

            ConnectivityStatus(vm)
            AppshackPokedexTheme(darkTheme = darkTheme.value) {
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary,
                            ),
                            title = {
                                Text(
                                    stringResource(id = R.string.app_name),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            navigationIcon = {
                                AnimatedVisibility(
                                    visible = canPop,
                                    enter = fadeIn(),
                                    exit = fadeOut()
                                ) {
                                    IconButton(onClick = {
                                        navController.navigateUp()
                                    }) {
                                        Icon(
                                            imageVector = Icons.Filled.ArrowBackIosNew,
                                            contentDescription = null
                                        )
                                    }
                                }
                            },
                            actions = {

                                IconButton(
                                    onClick = {
                                        vm.randomPokemon()
                                    }) {

                                    Icon(
                                        imageVector = Icons.Filled.Refresh,
                                        contentDescription = null
                                    )
                                }

                                val isDarkOn = darkTheme.value

                                IconButton(
                                    onClick = {
                                        darkTheme.value = !darkTheme.value
                                    }) {
                                    val icon =
                                        if (isDarkOn) Icons.Filled.LightMode else Icons.Filled.DarkMode
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = stringResource(id = R.string.light_dark)
                                    )
                                }
                            })
                    },
                    bottomBar = { BottomNavigationBar(navController) },
                    snackbarHost = { SnackbarHost(snackBarHostState) },
                ) {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it),
                        color = MaterialTheme.colorScheme.surfaceContainer
                    ) {

                        if (error.isNotEmpty()) {
                            LaunchedEffect(key1 = snackBarHostState.currentSnackbarData) {
                                lifecycleScope.launch {
                                    when (snackBarHostState.showSnackbar(
                                        error,
                                        getString(R.string.retry)
                                    )) {
                                        SnackbarResult.ActionPerformed -> {
                                            vm.cleanError()
                                        }

                                        SnackbarResult.Dismissed -> {
                                            vm.cleanError()
                                        }
                                    }
                                }
                            }
                        } else {
                            snackBarHostState.currentSnackbarData?.dismiss()
                        }

                        NavHost(navController, startDestination = "home") {
                            composable(
                                "home",
                                enterTransition = {
                                    slideIntoContainer(
                                        AnimatedContentTransitionScope.SlideDirection.End,
                                        tween(300)
                                    )
                                },
                                exitTransition = {
                                    slideOutOfContainer(
                                        AnimatedContentTransitionScope.SlideDirection.Start,
                                        tween(300)
                                    )
                                },
                            ) {
                                HomeScreen(vm, false)
                            }
                            composable(
                                "starred",
                                enterTransition = {
                                    slideIntoContainer(
                                        AnimatedContentTransitionScope.SlideDirection.End,
                                        tween(300)
                                    )
                                },
                                exitTransition = {
                                    slideOutOfContainer(
                                        AnimatedContentTransitionScope.SlideDirection.Start,
                                        tween(300)
                                    )
                                },
                            ) {
                                HomeScreen(vm, true)
                            }
                            composable(
                                "detail/{pokemonId}",
                                arguments = listOf(navArgument("pokemonId") {
                                    type = NavType.IntType
                                }),
                                enterTransition = {
                                    slideIntoContainer(
                                        AnimatedContentTransitionScope.SlideDirection.Start,
                                        tween(300)
                                    )
                                },
                                exitTransition = {
                                    slideOutOfContainer(
                                        AnimatedContentTransitionScope.SlideDirection.End,
                                        tween(300)
                                    )
                                },
                                popEnterTransition = {
                                    slideIntoContainer(
                                        AnimatedContentTransitionScope.SlideDirection.End,
                                        tween(300)
                                    )
                                },
                            ) { DetailsPager(vm) }
                        }
                        if (navToDetails) {
                            if (navController.currentDestination?.hasRoute("detail/{pokemonId}", null) == true){
                                navController.popBackStack("detail/{pokemonId}", true)
                            }

                            navController.navigate("detail/$pokemonId")
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Composable
    fun ConnectivityStatus(viewModel: MainViewModel) {
        // This will cause re-composition on every network state change
        val connection by connectivityState()
        val isConnected = connection == ConnectionState.Available
        viewModel.setIsConnected(isConnected)
    }

}