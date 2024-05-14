package com.example.kaaproperties.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class tenant(
    val phoneNumber: String,
    @PrimaryKey(autoGenerate = true)
    val tenantId: Int = 0,
    val fullName: String,
    val propertyId: Int,
    val Imageuri: String?,
    val email: String,
    val hasPaid: Boolean,
)
