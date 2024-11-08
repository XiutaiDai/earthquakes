package com.challenge.earthquakes.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenge.earthquakes.model.Earthquake
import com.challenge.earthquakes.network.EarthquakeService
import com.challenge.earthquakes.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EarthquakeViewModel(private val _testService: EarthquakeService? = null) : ViewModel() {
    private val _earthquakes = mutableStateListOf<Earthquake>()
    val earthquakes: List<Earthquake>
        get() = _earthquakes.toList()

    private val _loading = mutableStateOf(true)
    val loading: Boolean
        get() = _loading.value

    init {
        loadEarthquakes()
    }

    private fun loadEarthquakes() {
        viewModelScope.launch(Dispatchers.IO) {
            _loading.value = true
            try {
                val response =
                    if (_testService != null)
                        _testService.getEarthquakes()
                    else RetrofitClient.instance.getEarthquakes()

                if (response.isSuccessful) {
                    val earthquakeList =
                        response.body()?.features?.map { Earthquake(it.properties, it.geometry) }
                            ?: emptyList()
                    _earthquakes.addAll(earthquakeList)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _loading.value = false
            }
        }
    }
}