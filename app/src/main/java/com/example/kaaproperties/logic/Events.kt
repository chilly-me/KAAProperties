package com.example.kaaproperties.logic

import android.net.Uri
import com.example.kaaproperties.room.entities.location
import com.example.kaaproperties.room.entities.property
import com.example.kaaproperties.room.entities.tenant

interface Events {
    data class selectLocation(val locationId: Int, val locationName: String): Events
    object showLocations: Events
    object showProperies: Events
    object showTenants: Events
    data class saveLocation(val imagesUri: List<Uri?>) : Events
    data class saveProperty(val imagesUri: List<Uri?>): Events
    data class saveTenant(val imageUri: Uri): Events
    object Adding: Events
    object NotAdding: Events

//    object IsActive: Events
//    object NotActive: Events

    data class selectProperty(val propertyId: Int, val propertyName: String): Events


    data class setLocationName(val locationName: String): Events
    data class setLocationDescription(val locationDescription: String): Events
    data class setPropertyName(val propertyName: String): Events
    data class setpropertyDescription(val propertyDescription: String): Events
    data class setpropertyAddress(val propertyAddress: String): Events
    data class setcapacity(val capacity: String): Events
    data class setlocationId(val locationId: Int): Events
    data class setfullName(val fullName: String): Events
    data class setemail(val email: String): Events
    data class setphoneNumber(val phoneNumber: String): Events
    data class setpropertyId(val propertyId: Int): Events

    data class deleteLocation(val location: location): Events
    data class deleteProperty(val property: property): Events
    data class deleteTenant(val tenant: tenant): Events
    data class confirmRent(val hasPaid: Boolean, val tenantId: Int): Events

    data class selectTenant(val tenantId: Int): Events

    data class searchTenant(val tenantName: String): Events
    data class searchLocation(val locationName: String): Events

    data class searchProperties(val name: String): Events
    data class filterPropertiesbyDescription(val description: String, val locationId: Int): Events
    data class filterPropertiesbyCapacity(val capacity: String, val locationId: Int): Events





}