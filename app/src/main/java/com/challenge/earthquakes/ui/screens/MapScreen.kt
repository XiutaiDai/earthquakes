package com.challenge.earthquakes.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import android.text.format.DateFormat
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.amap.api.location.CoordinateConverter
import com.amap.api.location.CoordinateConverter.CoordType
import com.amap.api.location.DPoint
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amap.api.maps.MapsInitializer
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MarkerOptions
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    navController: NavHostController,
    coordinates: List<Double>,
    place: String,
    time: Long
) {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {

        }
    }
    DisposableEffect(Unit) {
        // 在 Composable 进入组合时执行
        mapView.onCreate(null)
        mapView.onResume()
        onDispose {
            // 在 Composable 离开组合时执行
            mapView.onPause()
            mapView.onDestroy()
        }
    }
    Scaffold(
        topBar = {
            TitleBar(time, place, navController)
        }
    ) { innerPadding ->

        Column(modifier = Modifier.fillMaxSize()) {
            // 坐标系转到高德坐标系
            val converter = CoordinateConverter(context)
            // CoordType.GPS 待转换坐标类型
            converter.from(CoordType.GPS)
            // sourceLatLng待转换坐标点 LatLng类型
            converter.coord(DPoint(coordinates[1], coordinates[0]))
            // 执行转换操作
            val desLatLng = converter.convert()
            // 使用地图库来显示地图和标记地震位置
            mapView.map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(desLatLng.latitude, desLatLng.longitude),
                    7f  // 初始缩放级别
                )
            )
            mapView.map.addMarker(MarkerOptions().position(LatLng(desLatLng.latitude, desLatLng.longitude)))
            AndroidView({ mapView }, Modifier.padding(innerPadding))
        }
    }
}

@SuppressLint("ObsoleteSdkInt")
@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TitleBar(
    time: Long,
    place: String,
    navController: NavHostController
) {
    TopAppBar(
        title = {
            val formattedDate: String
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                formattedDate = DateFormat.format("yyyy-MM-dd HH:mm:ss", time).toString()
            } else {
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                formattedDate = sdf.format(time)
            }
            Column {
                Text(place, style = MaterialTheme.typography.titleLarge)
                Text(formattedDate, style = MaterialTheme.typography.bodySmall)
            }
        },
        navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }
    )
}