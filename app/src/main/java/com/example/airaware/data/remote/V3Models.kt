package com.example.airaware.data.remote

data class LocationsResponse(
    val results: List<LocationResult>
)

data class LocationResult(
    val id: Int,
    val name: String,
    val datetimeLast: DatetimeObject?,
    val sensors: List<Sensor>?
)

data class DatetimeObject(
    val utc: String,
    val local: String
)

data class Sensor(
    val id: Int,
    val name: String,
    val parameter: Parameter
)

data class Parameter(
    val id: Int,
    val name: String,
    val units: String,
    val displayName: String?
)

data class LatestResponse(
    val results: List<LatestResult>
)

data class LatestResult(
    val sensorsId: Int,
    val value: Double,
    val datetime: DatetimeObject
)
