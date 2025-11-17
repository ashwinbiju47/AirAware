package com.example.airaware.data.remote
import com.example.airaware.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query

data class OpenAQResponse(
    val results: List<OpenAQResult>
)

data class OpenAQResult(
    val coordinates: Coordinates,
    val measurements: List<Measurement>
)

data class Coordinates(val latitude: Double, val longitude: Double)

data class Measurement(
    val parameter: String,
    val value: Double,
    val unit: String
)

interface AirQualityService {

    @GET("v3/latest")
    suspend fun getAirQuality(
        @Query("coordinates") coordinates: String,
        @Query("apikey") apiKey: String = BuildConfig.OPENAQ_API_KEY
    ): OpenAQResponse

}
