package com.example.airaware.data.remote

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

data class NominatimResponse(
    val address: Address?
)

data class Address(
    val country: String?,
    val country_code: String?
)

interface NominatimService {
    @GET("reverse")
    suspend fun reverseGeocode(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("format") format: String = "json",
        @Header("User-Agent") userAgent: String = "AirAware/1.0"
    ): NominatimResponse
}
