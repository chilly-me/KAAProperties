package com.example.kaaproperties.logic

import com.example.kaaproperties.room.entities.location
import com.example.kaaproperties.room.entities.property
import com.example.kaaproperties.room.entities.tenant
import kotlinx.coroutines.flow.Flow

data class states(
    val locations: List<location> = emptyList(),
    val property: List<property> = emptyList(),
    val tenants: List<tenant> = emptyList(),
    val locationName: String = "",
    val locationDescription: String = "",
    val propertyName: String = "",
    val propertyDescription: String = "",
    val propertyAddress: String = "",
    val capacity: String = "",
    val locationId: Int = 0,
    val fullName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val propertyId: Int = 0,
    val isAdding: Boolean = false
)
