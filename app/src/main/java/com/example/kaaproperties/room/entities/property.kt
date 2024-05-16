package com.example.kaaproperties.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

//@Entity
//data class property(
//    val propertyName: String,
//    val propertyDescription: String,
//    val propertyAddress: String,
//    val propertyImages: ArrayList<String>,
//    val capacity: String,
//    val locationId: Int,
//    @PrimaryKey(autoGenerate = true)
//    val propertyId: Int = 0,
//)

@Entity
data class property(
    val propertyName: String,
    val propertyDescription: String,
    val propertyAddress: String,
    val propertyImages: ArrayList<String>,
    val capacity: String,
    val cost: String,
    val locationId: Int,
    @PrimaryKey(autoGenerate = true)
    val propertyId: Int = 0,
)
