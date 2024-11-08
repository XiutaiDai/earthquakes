package com.challenge.earthquakes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.challenge.earthquakes.ui.screens.Navigation
import com.challenge.earthquakes.ui.theme.EarthquakesTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EarthquakesTheme {
                Navigation()
            }
        }
    }
}
