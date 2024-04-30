package com.example.kaaproperties

import com.example.kaaproperties.room.entities.location
import com.example.kaaproperties.room.entities.property
import com.example.kaaproperties.room.entities.tenant

interface Events {
    data class selectLocation(val locationId: Int): Events
    object showLocations: Events
    object showProperies: Events
    object saveLocation: Events
    object saveProperty: Events
    object saveTenant: Events
    object Adding: Events
    object NotAdding: Events

    data class selectProperty(val propertyId: Int): Events

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

}