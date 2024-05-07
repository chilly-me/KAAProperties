package com.example.kaaproperties.flows

import com.example.kaaproperties.room.relations.propertyWithTenants
import kotlinx.coroutines.flow.Flow

interface DBhelper {

    fun getTenants(propertyId: Int): Flow<List<propertyWithTenants>>
}