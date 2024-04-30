package com.example.kaaproperties.room.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.kaaproperties.room.entities.property
import com.example.kaaproperties.room.entities.tenant

data class propertyWithTenants(
    @Embedded val property: property,
    @Relation(
        parentColumn = "propertyId",
        entityColumn = "propertyId"
    )
    val tenants: List<tenant>
)
