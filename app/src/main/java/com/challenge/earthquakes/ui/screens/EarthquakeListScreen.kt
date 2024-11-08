package com.challenge.earthquakes.ui.screens

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.text.format.DateFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.amap.api.maps.MapsInitializer
import com.challenge.earthquakes.model.Earthquake
import com.challenge.earthquakes.viewmodel.EarthquakeViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EarthquakeListScreen(navController: NavController, viewModel: EarthquakeViewModel) {
    val context = LocalContext.current
    val loading = viewModel.loading
    val earthquakes = viewModel.earthquakes

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("USGS Earthquakes") })
        },
        containerColor = MaterialTheme.colorScheme.inversePrimary
    ) { innerPadding ->
        when {
            loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingIndicator()
                }
            }

            earthquakes.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    NoDataText()
                }
            }

            else -> {
                LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = innerPadding) {
                    items(earthquakes.size) { idx ->
                        val earthquake = earthquakes[idx]
                        EarthquakeListItem(
                            earthquake = earthquake,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable {
                                    navigateToMapScreen(navController, context, earthquake)
                                }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingIndicator() {
    Column {
        CircularProgressIndicator(
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.CenterHorizontally)
        )
        Text(
            "数据加载中...",
            Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun NoDataText() {
    Text(
        "没有数据...",
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.titleLarge
    )
}

fun navigateToMapScreen(
    navController: NavController,
    context: Context,
    earthquake: Earthquake
) {
    val sharedPreferences = context.getSharedPreferences("PrivacyPrefs", Context.MODE_PRIVATE)
    val hasShownPrivacyDialog = sharedPreferences.getBoolean("hasShownPrivacyDialog", false)

    if (!hasShownPrivacyDialog) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("高德地图隐私政策")
        builder.setMessage("我们需要您同意高德地图的隐私政策才能使用地图功能。以下是隐私政策的详细内容...")
        builder.setPositiveButton("同意") { _, _ ->
            val editor = sharedPreferences.edit()
            editor.putBoolean("hasShownPrivacyDialog", true)
            editor.apply()
            initializeMapSDK(context)
            navigateToMap(navController, earthquake)
        }
        builder.setNegativeButton("不同意") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    } else {
        initializeMapSDK(context)
        navigateToMap(navController, earthquake)
    }
}

private fun initializeMapSDK(context: Context) {
    MapsInitializer.initialize(context)
    MapsInitializer.updatePrivacyShow(context, true, true)
    MapsInitializer.updatePrivacyAgree(context, true)
}

private fun navigateToMap(navController: NavController, earthquake: Earthquake) {
    val coordinates = earthquake.geometry.coordinates
    val place = earthquake.properties.place
    val time = earthquake.properties.time
    navController.navigate("earthquake_map/${coordinates}/${place}/${time}")
}

@SuppressLint("ObsoleteSdkInt")
@Composable
fun EarthquakeListItem(
    earthquake: Earthquake,
    modifier: Modifier
) {
    val magnitudeColor = if (earthquake.properties.mag >= 7.5) {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.colorScheme.secondary
    }
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = magnitudeColor)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            val formattedDate: String
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                formattedDate =
                    DateFormat.format("yyyy-MM-dd HH:mm:ss", earthquake.properties.time).toString()
            } else {
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                formattedDate = sdf.format(earthquake.properties.time)
            }
            Text(
                text = earthquake.properties.place,
                style = MaterialTheme.typography.titleLarge
            )
            Row {
                Text(
                    text = "Magnitude: ${earthquake.properties.mag}",
                    textAlign = TextAlign.Start
                )
                Text(
                    text = formattedDate,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

}
