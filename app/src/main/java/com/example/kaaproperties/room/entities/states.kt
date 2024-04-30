package com.example.kaaproperties.room.entities

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
