package com.example.kaaproperties.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class location(
    val locationName: String,
    val description: String,
    @PrimaryKey(autoGenerate = true)
    val locationId: Int = 0,
    val locationImages: ArrayList<String>
)