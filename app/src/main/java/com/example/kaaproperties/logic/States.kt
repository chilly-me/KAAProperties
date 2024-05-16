package com.example.kaaproperties.logic

import com.example.kaaproperties.room.entities.location
import com.example.kaaproperties.room.entities.payments
import com.example.kaaproperties.room.entities.property
import com.example.kaaproperties.room.entities.tenant

data class states(
    val locations: List<location> = emptyList(),
    val selectedTenant: tenant = tenant(fullName = "", email = "", phoneNumber = "", propertyId = 0, Imageuri = "", hasPaid = false),
    val payments: List<payments> = emptyList(),
    val selectedProperty: property = property(propertyName = "", propertyDescription = "", propertyAddress = "", propertyImages = ArrayList<String>(), capacity = "", locationId = 0, cost = ""),
    val selectedLocation: location = location(locationName = "", description = "", locationId = 0, locationImages = ArrayList<String>()),
    val property: List<property> = emptyList(),
    val tenants: List<tenant> = emptyList(),
    val locationName: String = "",
    val locationDescription: String = "",
    val locationImages: List<String> = emptyList(),
    val propertyName: String = "",
    val propertyDescription: String = "",
    val propertyAddress: String = "",
    val capacity: String = "",
    val propertyCost: String = "",
    val locationId: Int = 0,
    val fullName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val propertyId: Int = 0,
    val tenantImage: String = "",
    val uri: String = "",
    val isAdding: Boolean = false,
    val hasPaidRent: Boolean = false,
    val active: Boolean = true,
    val key: Int = 0
)
