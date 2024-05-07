package com.example.kaaproperties.flows

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import com.example.kaaproperties.logic.states
import com.example.kaaproperties.room.dao.PropertyDao
import com.example.kaaproperties.room.database.PropertyDatabase
import com.example.kaaproperties.room.relations.propertyWithTenants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DBHelperImpl(private val dao: PropertyDao, states: states): DBhelper {
    override fun getTenants(propertyId: Int): Flow<List<propertyWithTenants>> = flow {
        emit(dao.getPropertyWithTenants(propertyId))
    }

}