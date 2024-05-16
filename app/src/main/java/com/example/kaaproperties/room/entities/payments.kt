package com.example.kaaproperties.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class payments(
    val tenantId: Int,
    val time: String = System.currentTimeMillis().toString(),
    val amount: String,
    val month: String,
    @PrimaryKey(autoGenerate = true)
    val paymentId: Int,
)
