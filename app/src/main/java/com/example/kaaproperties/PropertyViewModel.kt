package com.example.kaaproperties

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kaaproperties.room.dao.PropertyDao
import com.example.kaaproperties.room.entities.location
import com.example.kaaproperties.room.entities.property
import com.example.kaaproperties.room.entities.states
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PropertyViewModel(private val dao: PropertyDao): ViewModel() {
    private val _state = MutableStateFlow(states())
    val state = _state

    fun onEvent(events: Events){
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


            }
            is Events.selectLocation ->{

            }
            is Events.selectProperty ->{
                viewModelScope.launch {
                    val propertyList = dao.getLocationWithProperties(events.propertyId).flatMap { it.properties }
                    _state.update { it.copy(
                        property = propertyList
                    ) }
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

            }
            is Events.Adding ->{
                _state.update { it.copy(
                    isAdding = true
                ) }
            }
            is Events.NotAdding ->{
                _state.update { it.copy(
                    isAdding = false
                ) }
            }

        }
    }
}