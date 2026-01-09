package com.example.airaware.data.remote
import com.example.airaware.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
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

    @GET("v3/locations")
    suspend fun getLocations(
        @Query("countries_id") countryId: Int,
        @Query("limit") limit: Int = 100,
        @Query("order_by") orderBy: String = "id",
        @Query("sort") sort: String = "desc",
        @Header("X-API-Key") apiKey: String = BuildConfig.OPENAQ_API_KEY
    ): LocationsResponse

    @GET("v3/locations/{id}/latest")
    suspend fun getLocationLatest(
        @Path("id") locationId: Int,
        @Header("X-API-Key") apiKey: String = BuildConfig.OPENAQ_API_KEY
    ): LatestResponse


}



