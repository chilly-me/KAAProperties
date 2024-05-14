package com.example.kaaproperties.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.kaaproperties.room.entities.location
import com.example.kaaproperties.room.entities.property
import com.example.kaaproperties.room.entities.tenant
import com.example.kaaproperties.room.relations.locationWithProperties
import com.example.kaaproperties.room.relations.propertyWithTenants
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertyDao {

    @Query("UPDATE tenant SET hasPaid = :hasPaid WHERE tenantId = :tenantId")
    suspend fun updateTenant(tenantId: Int, hasPaid:Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: location): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProperty(property: property)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTenant(tenant: tenant)

    @Query("DELETE FROM tenant")
    suspend fun deleteAllTenants()

    @Query("DELETE FROM property")
    suspend fun deleteAllProperties()

    @Query("DELETE FROM location")
    suspend fun deleteAllLocations()
    @Delete
    suspend fun deleteLocation(location: location)

    @Delete
    suspend fun deleteProperty(property: property)

    @Delete
    suspend fun deleteTenant(tenant: tenant)

    @Query("SELECT * FROM location")
    fun getAllLocations(): Flow<List<location>>

    @Query("SELECT * FROM property")
    fun getAllProperties(): Flow<List<property>>

    @Query("SELECT * FROM tenant")
    fun getAllTenants(): Flow<List<tenant>>

    @Query("SELECT * FROM tenant WHERE fullName LIKE :name|| '%' ")
    fun searchForTenants(name: String): Flow<List<tenant>>

    @Query("SELECT * FROM location WHERE locationName LIKE :name|| '%' ")
    fun searchForLocations(name: String): Flow<List<location>>

    @Query("SELECT * FROM property WHERE propertyDescription = :description AND locationId = :locationId")
    fun filterPropertiesUsingDescription(description: String, locationId: Int): Flow<List<property>>

    @Query("SELECT * FROM property WHERE capacity = :capacity AND locationId = :locationId")
    fun filterPropertiesUsingCapcity(capacity: String, locationId: Int): Flow<List<property>>

    @Query("SELECT * FROM property WHERE propertyName LIKE :text|| '%'")
    fun searchForProperties(text: String): Flow<List<property>>



    @Query("SELECT * FROM tenant WHERE tenantId = :tenantId")
    suspend fun getTenant(tenantId: Int): tenant

    @Transaction
    @Query("SELECT * FROM location WHERE locationId = :locationId")
    fun getLocationWithProperties(locationId: Int): Flow<List<locationWithProperties>>

    @Transaction
    @Query("SELECT * FROM property WHERE propertyId = :propertyId")
    fun getPropertyWithTenants(propertyId: Int): Flow<List<propertyWithTenants>>
}