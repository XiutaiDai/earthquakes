package com.challenge.earthquakes.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.challenge.earthquakes.viewmodel.EarthquakeViewModel

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val viewModel = EarthquakeViewModel()

    NavHost(
        navController = navController,
        startDestination = "earthquake_list"
    ) {
        composable("earthquake_list") { EarthquakeListScreen(navController, viewModel) }
        composable("earthquake_map/{coordinates}/{place}/{time}") { backStackEntry ->
            val coordinates = backStackEntry.arguments?.getString("coordinates")
                ?.replace("[", "")
                ?.replace("]", "")
                ?.split(",")
                ?.map(String::toDouble)
                ?: emptyList()
            val place = backStackEntry.arguments?.getString("place") ?: ""
            val time = backStackEntry.arguments?.getString("time")?.toLong() ?: 0L
            MapScreen(navController, coordinates, place, time)
        }
    }
}

