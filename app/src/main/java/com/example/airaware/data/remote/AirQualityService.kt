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

    @GET("v3/countries/{id}")
    suspend fun getCountryDetails(
        @Path("id") countryId: Int,
        @Header("X-API-Key") apiKey: String = BuildConfig.OPENAQ_API_KEY
    ): CountryResponse


}


data class CountryResponse(
    val meta: Meta,
    val results: List<CountryResult>
)

data class Meta(
    val name: String,
    val website: String?,
    val page: Int,
    val limit: Int,
    val found: Int
)

data class CountryResult(
    val id: Int,
    val code: String,
    val name: String,
    val datetimeFirst: String?,
    val datetimeLast: String?,
    val parameters: List<Parameter>
)

data class Parameter(
    val id: Int,
    val name: String,
    val units: String,
    val displayName: String?
)
