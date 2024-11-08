package com.challenge.earthquakes.network

import com.challenge.earthquakes.model.EarthquakeResponse
import retrofit2.Response
import retrofit2.http.GET

interface EarthquakeService {
    @GET("query?format=geojson&starttime=2023-01-01&endtime=2024-01-01&minmagnitude=7")
    suspend fun getEarthquakes(): Response<EarthquakeResponse>
}