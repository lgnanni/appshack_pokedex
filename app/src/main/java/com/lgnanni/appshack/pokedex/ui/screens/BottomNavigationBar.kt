package com.lgnanni.appshack.pokedex.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.lgnanni.appshack.pokedex.R

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    BottomAppBar {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = null) },
            label = { Text(stringResource(id = R.string.home)) },
            selected = false,
            onClick = {
                //Prevent trying to show the page when its already there
                val currentNav = navController.currentDestination?.route

                currentNav?.let {
                    if (it.contains("home"))
                        return@NavigationBarItem
                    else {

                        val destinationExists = navController.graph.findNode("detail/{pokemonId}") != null
                        if (destinationExists) {
                            navController.popBackStack("detail/{pokemonId}", true)
                        }

                        navController.navigate("home") {
                            launchSingleTop = true
                            popUpTo("home") {
                                inclusive = true
                            }
                        }
                    }
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Stars, contentDescription = null) },
            label = { Text(stringResource(id = R.string.starred)) },
            selected = false,
            onClick = {
                //Prevent trying to show the page when its already there
                val currentNav = navController.currentDestination?.route

                currentNav?.let {
                    if (it.contains("starred"))
                        return@NavigationBarItem
                }

                val destinationExists = navController.graph.findNode("detail/{pokemonId}") != null
                if (destinationExists) {
                    navController.popBackStack("detail/{pokemonId}", true)
                }

                navController.navigate("starred") {
                    launchSingleTop = true
                    popUpTo("starred") {
                        inclusive = true
                    }

                }
            }
        )
    }
}
