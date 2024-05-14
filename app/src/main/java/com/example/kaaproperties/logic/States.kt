package com.example.kaaproperties.logic

import com.example.kaaproperties.room.entities.location
import com.example.kaaproperties.room.entities.property
import com.example.kaaproperties.room.entities.tenant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class states(
    val locations: List<location> = emptyList(),
    val selectedtenant: tenant = tenant(fullName = "", email = "", phoneNumber = "", propertyId = 0, Imageuri = "", hasPaid = false),
    val property: List<property> = emptyList(),
    val tenants: List<tenant> = emptyList(),
    val locationName: String = "",
    val locationDescription: String = "",
    val locationImages: List<String> = emptyList(),
    val propertyName: String = "",
    val propertyDescription: String = "",
    val propertyAddress: String = "",
    val capacity: String = "",
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
