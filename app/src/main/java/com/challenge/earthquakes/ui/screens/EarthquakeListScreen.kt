package com.challenge.earthquakes.ui.screens

import android.annotation.SuppressLint
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
            TopAppBar(
                title = { Text("USGS Earthquakes") },
            )
        }, containerColor = MaterialTheme.colorScheme.inversePrimary
    ) { innerPadding ->
        if (loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp).align(Alignment.CenterHorizontally)
                    )
                    Text("数据加载中...",
                        Modifier.padding(8.dp).align(Alignment.CenterHorizontally),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            return@Scaffold
        }
        if (earthquakes.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("没有数据...",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            return@Scaffold
        }
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = innerPadding) {
            for (earthquake in earthquakes) {
                item {
                    EarthquakeListItem(
                        earthquake = earthquake,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                // TODO:需弹窗提示用户高德地图隐私政策，并要求同意，初始化地图 SDK, 此处暂时默认己提示并同意隐私政策
                                MapsInitializer.initialize(context)
                                MapsInitializer.updatePrivacyShow(context, true, true)
                                MapsInitializer.updatePrivacyAgree(context, true)
                                // 导航到显示地图的屏幕，传递地震位置信息
                                val coordinates = earthquake.geometry.coordinates
                                val place = earthquake.properties.place
                                val time = earthquake.properties.time
                                navController.navigate("earthquake_map/${coordinates}/${place}/${time}")

                            },
                    )
                }
            }
        }
    }
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
