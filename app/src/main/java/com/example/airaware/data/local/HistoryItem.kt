package com.example.airaware.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_items")
data class HistoryItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val countryName: String,
    val timestamp: Long,
    val details: String // Storing simplified details for now
)
