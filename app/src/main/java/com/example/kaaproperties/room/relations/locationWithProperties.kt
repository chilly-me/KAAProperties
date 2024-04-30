package com.example.kaaproperties.room.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.kaaproperties.room.entities.location
import com.example.kaaproperties.room.entities.property

data class locationWithProperties(
    @Embedded val location: location,
    @Relation(
        parentColumn = "locationId",
        entityColumn = "locationId"
    )
    val properties: List<property>
)
