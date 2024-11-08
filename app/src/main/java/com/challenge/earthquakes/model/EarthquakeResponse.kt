package com.challenge.earthquakes.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EarthquakeResponse(
    @Json(name = "type") val type: String,
    @Json(name = "metadata") val metadata: Metadata,
    @Json(name = "features") val features: List<Feature>,
    @Json(name = "bbox") val bbox: List<Double>?
)

@JsonClass(generateAdapter = true)
data class Metadata(
    @Json(name = "generated") val generated: Long,
    @Json(name = "url") val url: String,
    @Json(name = "title") val title: String,
    @Json(name = "status") val status: Int,
    @Json(name = "api") val api: String,
    @Json(name = "count") val count: Int
)

@JsonClass(generateAdapter = true)
data class Feature(
    @Json(name = "type") val type: String,
    @Json(name = "properties") val properties: Properties,
    @Json(name = "geometry") val geometry: Geometry
)

@JsonClass(generateAdapter = true)
data class Properties(
    @Json(name = "mag") val mag: Double,
    @Json(name = "place") val place: String,
    @Json(name = "time") val time: Long,
    @Json(name = "updated") val updated: Long,
    @Json(name = "tz") val tz: Int?,
    @Json(name = "url") val url: String,
    @Json(name = "detail") val detail: String,
    @Json(name = "felt") val felt: Int?,
    @Json(name = "cdi") val cdi: Double?,
    @Json(name = "mmi") val mmi: Double?,
    @Json(name = "alert") val alert: String?,
    @Json(name = "status") val status: String,
    @Json(name = "tsunami") val tsunami: Int,
    @Json(name = "sig") val sig: Int,
    @Json(name = "net") val net: String,
    @Json(name = "code") val code: String,
    @Json(name = "ids") val ids: String,
    @Json(name = "sources") val sources: String,
    @Json(name = "types") val types: String,
    @Json(name = "nst") val nst: Int?,
    @Json(name = "dmin") val dmin: Double?,
    @Json(name = "rms") val rms: Double,
    @Json(name = "gap") val gap: Double?,
    @Json(name = "magType") val magType: String,
    @Json(name = "type") val eventType: String,
    @Json(name = "title") val title: String
)

@JsonClass(generateAdapter = true)
data class Geometry(
    @Json(name = "type") val type: String,
    @Json(name = "coordinates") val coordinates: List<Double> // [longitude, latitude, depth]
)