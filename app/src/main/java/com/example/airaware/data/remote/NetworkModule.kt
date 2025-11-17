package com.example.airaware.data.remote

object NetworkModule {

    private const val BASE_URL = "https://api.openaq.org/"

    val retrofit = retrofit2.Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
        .build()

    val airService: AirQualityService = retrofit.create(AirQualityService::class.java)
}
