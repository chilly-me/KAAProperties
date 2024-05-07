package com.example.kaaproperties

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kaaproperties.flows.DBHelperImpl
import com.example.kaaproperties.flows.DBhelper
import com.example.kaaproperties.logic.Events
import com.example.kaaproperties.room.dao.PropertyDao
import com.example.kaaproperties.room.entities.location
import com.example.kaaproperties.room.entities.property
import com.example.kaaproperties.logic.states
import com.example.kaaproperties.room.entities.tenant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PropertyViewModel(private val dao: PropertyDao): ViewModel() {

    var property_loading = mutableStateOf(false)

    fun isLoading(){
        property_loading.value = true
    }
    fun isNotLoading(){
        property_loading.value = false
    }

    private val _state = MutableStateFlow(states())
    val state = _state
    fun onEvent(events: Events, dbHelper: DBhelper = DBHelperImpl(dao, state.value)){
        when(events){
            is Events.showLocations ->{
                viewModelScope.launch {
                    val locationList = dao.getAllLocations()

                    _state.update { it.copy(
                        locations = locationList
                    )

                    }
                }
            }
            is Events.showProperies ->{
                viewModelScope.launch {
                    val allProperties = dao.getAllProperties()
                    _state.update { it.copy(
                        property = allProperties
                    ) }
                }

            }
            is Events.showTenants ->{
                viewModelScope.launch {
                    val allTenants = dao.getAllTenants()
                    _state.update { it.copy(
                        tenants = allTenants
                    ) }
                }

            }
            is Events.selectLocation ->{
                viewModelScope.launch {
                    val propertyList = dao.getLocationWithProperties(events.locationId).flatMap { it.properties }
                    _state.update { it.copy(
                        property = propertyList
                    ) }
                }
            }
            is Events.selectProperty ->{
                viewModelScope.launch {
                    dbHelper.getTenants(events.propertyId)
                        .flowOn(Dispatchers.IO)
                        .catch {e->
                            Log.d("ErrorOnFlow", "$e")
                        }
                        .collect{listofTenants ->
                            _state.update { it.copy(
                                tenants = listofTenants.flatMap { it.tenants }
                            )
                            }

                        }



                }
            }
            is Events.setLocationName ->{
                _state.update { it.copy(
                    locationName = events.locationName
                ) }
            }
            is Events.setLocationDescription ->{
                _state.update { it.copy(
                    locationDescription = events.locationDescription
                ) }
            }
            is Events.setPropertyName ->{
                _state.update { it.copy(
                    propertyName = events.propertyName
                ) }
            }
            is Events.setpropertyDescription ->{
                _state.update { it.copy(
                    propertyDescription = events.propertyDescription
                ) }
            }
            is Events.setpropertyAddress ->{
                _state.update { it.copy(
                    propertyAddress = events.propertyAddress
                ) }
            }
            is Events.setcapacity ->{
                _state.update { it.copy(
                   capacity = events.capacity
                ) }
            }
            is Events.setlocationId ->{
                _state.update { it.copy(
                    locationId = events.locationId
                ) }
            }
            is Events.setfullName ->{
                _state.update { it.copy(
                    fullName = events.fullName
                ) }
            }
            is Events.setemail ->{
                _state.update { it.copy(
                    email = events.email
                ) }
            }
            is Events.setphoneNumber ->{
                _state.update { it.copy(
                    phoneNumber = events.phoneNumber
                ) }
            }
            is Events.setpropertyId ->{
                _state.update { it.copy(
                    propertyId = events.propertyId
                ) }
            }
            is Events.saveLocation ->{
                val locationName = _state.value.locationName
                val locationDescription = _state.value.locationDescription
                if (locationName.isBlank() || locationDescription.isBlank()){
                    return
                }
                val location = location(
                    locationName = locationName,
                    description = locationDescription
                )
                viewModelScope.launch {
                    dao.insertLocation(location)
                }
                _state.update { it.copy(
                    locationName = "",
                    locationDescription = ""
                )
                }
            }
            is Events.saveProperty ->{
                val propertyName = _state.value.propertyName
                val propertyDescription= _state.value.propertyDescription
                val capacity = _state.value.capacity
                val locationId = _state.value.locationId
                val propertyAddress = _state.value.propertyAddress
                if (propertyName.isBlank() || propertyDescription.isBlank() || capacity.isBlank() || propertyAddress.isBlank() ){
                    return
                }
                val property = property(
                    propertyName = propertyName,
                    propertyDescription = propertyDescription,
                    capacity = capacity,
                    locationId = locationId,
                    propertyAddress = propertyAddress
                )
                viewModelScope.launch {
                    dao.insertProperty(property)
                }
                _state.update { it.copy(
                    propertyName = "",
                    propertyDescription = "",
                    capacity = "",
                    locationId = 0,
                    propertyAddress = ""
                ) }

            }
            is Events.saveTenant ->{
                val fullName = _state.value.fullName
                val email = _state.value.email
                val phoneNumber = _state.value.phoneNumber
                val propertyId = _state.value.propertyId
                val hasPaid = _state.value.hasPaidRent

                val tenant = tenant(fullName, email, phoneNumber, propertyId = propertyId, hasPaid =hasPaid)

                viewModelScope.launch {
                    dao.insertTenant(tenant)
                }
                _state.update { it.copy(
                    fullName = "",
                    email = "",
                    phoneNumber = "",
                    propertyId = 0
                ) }
            }
            is Events.Adding ->{
                _state.update {it.copy(
                    isAdding = true
                )
                }
            }
            is Events.NotAdding ->{
                _state.update { it.copy(
                    isAdding = false
                )
                }
            }
            is Events.deleteLocation ->{
                viewModelScope.launch {
                    dao.deleteLocation(events.location)
                }
            }
            is Events.deleteProperty ->{
                viewModelScope.launch {
                    dao.deleteProperty(events.property)
                }
            }
            is Events.deleteTenant ->{
                viewModelScope.launch {
                    dao.deleteTenant(events.tenant)
                }
            }
            is Events.confirmRent ->{
                viewModelScope.launch{
                    dao.updateTenant(tenantId = events.tenantId, hasPaid = events.hasPaid)
                }
            }
            is Events.selectTenant ->{
                viewModelScope.launch {
                    val tenant_selected = dao.getTenant(events.tenantId)
                    _state.update { it.copy(
                        selectedtenant = tenant_selected
                    ) }
                }
            }

        }
    }
}