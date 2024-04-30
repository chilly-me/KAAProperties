package com.example.kaaproperties.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.kaaproperties.room.entities.location
import com.example.kaaproperties.room.entities.property
import com.example.kaaproperties.room.entities.tenant
import com.example.kaaproperties.room.relations.locationWithProperties
import com.example.kaaproperties.room.relations.propertyWithTenants
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertyDao {
    @Query("DELETE FROM tenant")
    suspend fun nukeTable()

    @Query("DELETE FROM property")
    suspend fun nukeTable2()
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: location)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProperty(property: property)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTenant(tenant: tenant)
    @Query("SELECT * FROM location")
    suspend fun getAllLocations(): List<location>

    @Query("SELECT * FROM property")
    suspend fun getAllProperties(): List<property>


    @Transaction
    @Query("SELECT * FROM location WHERE locationId = :locationId")
    suspend fun getLocationWithProperties(locationId: Int): List<locationWithProperties>

    @Transaction
    @Query("SELECT * FROM property WHERE propertyId = :propertyId")
    suspend fun getPropertyWithTenants(propertyId: Int): List<propertyWithTenants>
}