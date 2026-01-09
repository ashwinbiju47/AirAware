package com.example.airaware.utils

import androidx.compose.ui.graphics.Color
import kotlin.math.roundToInt

data class AqiResult(
    val value: Int,
    val level: String,
    val color: Color,
    val message: String
)

object AqiUtils {

    fun calculateAQI(pm25: Double?, pm10: Double?): AqiResult? {
        val aqiPm25 = pm25?.let { calculatePm25Aqi(it) } ?: -1
        val aqiPm10 = pm10?.let { calculatePm10Aqi(it) } ?: -1

        val maxAqi = maxOf(aqiPm25, aqiPm10)

        if (maxAqi < 0) return null

        return getAqiAttributes(maxAqi)
    }

    private fun getAqiAttributes(aqi: Int): AqiResult {
        return when (aqi) {
            in 0..50 -> AqiResult(aqi, "Good", Color(0xFF4CAF50), "Air quality is satisfactory.")
            in 51..100 -> AqiResult(aqi, "Moderate", Color(0xFFFFEB3B), "Air quality is acceptable.")
            in 101..150 -> AqiResult(aqi, "Unhealthy for Sensitive Groups", Color(0xFFFF9800), "Members of sensitive groups may experience health effects.")
            in 151..200 -> AqiResult(aqi, "Unhealthy", Color(0xFFF44336), "Everyone may begin to experience health effects.")
            in 201..300 -> AqiResult(aqi, "Very Unhealthy", Color(0xFF9C27B0), "Health warnings of emergency conditions.")
            else -> AqiResult(aqi, "Hazardous", Color(0xFF880E4F), "Health alert: everyone may experience more serious health effects.")
        }
    }

    // US EPA Breakpoints for PM2.5
    private fun calculatePm25Aqi(conc: Double): Int {
        val c = conc.toFloat()
        return when {
            c <= 12.0 -> linear(50, 0, 12.0, 0.0, c)
            c <= 35.4 -> linear(100, 51, 35.4, 12.1, c)
            c <= 55.4 -> linear(150, 101, 55.4, 35.5, c)
            c <= 150.4 -> linear(200, 151, 150.4, 55.5, c)
            c <= 250.4 -> linear(300, 201, 250.4, 150.5, c)
            c <= 350.4 -> linear(400, 301, 350.4, 250.5, c)
            c <= 500.4 -> linear(500, 401, 500.4, 350.5, c)
            else -> 500 // Cap at 500
        }
    }

    // US EPA Breakpoints for PM10
    private fun calculatePm10Aqi(conc: Double): Int {
        val c = conc.toFloat()
        return when {
            c <= 54 -> linear(50, 0, 54.0, 0.0, c)
            c <= 154 -> linear(100, 51, 154.0, 55.0, c)
            c <= 254 -> linear(150, 101, 254.0, 155.0, c)
            c <= 354 -> linear(200, 151, 354.0, 255.0, c)
            c <= 424 -> linear(300, 201, 424.0, 355.0, c)
            c <= 504 -> linear(400, 301, 504.0, 425.0, c)
            c <= 604 -> linear(500, 401, 604.0, 505.0, c)
            else -> 500
        }
    }

    private fun linear(aqiHigh: Int, aqiLow: Int, concHigh: Double, concLow: Double, conc: Float): Int {
        return (((aqiHigh - aqiLow) / (concHigh - concLow)) * (conc - concLow) + aqiLow).roundToInt()
    }
}
