package com.challenge.earthquakes.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://earthquake.usgs.gov/fdsnws/event/1/"

    val instance: EarthquakeService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        retrofit.create(EarthquakeService::class.java)
    }
}