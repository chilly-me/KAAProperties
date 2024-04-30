package com.example.kaaproperties.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class tenant(
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val propertyId: Int,
    @PrimaryKey(autoGenerate = true)
    val tenantId: Int = 0,
)
