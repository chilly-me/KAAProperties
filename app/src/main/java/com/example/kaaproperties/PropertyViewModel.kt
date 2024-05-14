package com.example.kaaproperties

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kaaproperties.logic.Events
import com.example.kaaproperties.room.dao.PropertyDao
import com.example.kaaproperties.room.entities.location
import com.example.kaaproperties.room.entities.property
import com.example.kaaproperties.logic.states
import com.example.kaaproperties.room.entities.tenant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class PropertyViewModel(
    private val dao: PropertyDao,
    private var auth: FirebaseAuth,
    private var firebaseFirestore: FirebaseFirestore,
    private var storageRef: StorageReference,
    private var context: Context,
) : ViewModel() {

    var loading = mutableStateOf(false)

    fun isLoading() {
        loading.value = true
    }

    fun isNotLoading() {
        loading.value = false
    }


    private val _state = MutableStateFlow(states())
    val state = _state


    @SuppressLint("MutableCollectionMutableState")
    fun onEvent(events: Events) {

        when (events) {
            is Events.showLocations -> {
                viewModelScope.launch {
                    dao.getAllLocations()
                        .flowOn(Dispatchers.IO)
                        .catch { e ->
                            Log.d("ErrorOnFlow", "$e")
                        }
                        .collect { locations ->
                            _state.update {
                                it.copy(
                                    locations = locations
                                )
                            }


                        }
                }
            }

            is Events.showProperies -> {
                viewModelScope.launch {
                    dao.getAllProperties()
                        .flowOn(Dispatchers.IO)
                        .catch { e ->
                            Log.d("ErrorOnFlow", "$e")
                        }
                        .collect { properties ->
                            _state.update {
                                it.copy(
                                    property = properties
                                )
                            }
                        }

                }

            }

            is Events.showTenants -> {
                viewModelScope.launch {
                    dao.getAllTenants()
                        .flowOn(Dispatchers.IO)
                        .catch { e ->
                            Log.d("ErrorOnFlow", "$e")
                        }
                        .collect { tenants ->
                            _state.update {
                                it.copy(
                                    tenants = tenants
                                )
                            }
                        }
                }

            }

            is Events.selectLocation -> {

                viewModelScope.launch {
                    dao.getLocationWithProperties(events.locationId)
                        .flowOn(Dispatchers.IO)
                        .catch { e ->
                            Log.d("ErrorOnFlow", "$e")
                        }
                        .collect { listofProperties ->
                            _state.update {
                                it.copy(
                                    property = listofProperties.flatMap { it.properties },
                                    locationName = events.locationName
                                )
                            }
                        }
                }
            }

            is Events.selectProperty -> {
                viewModelScope.launch {
                    dao.getPropertyWithTenants(events.propertyId)
                        .flowOn(Dispatchers.IO)
                        .catch { e ->
                            Log.d("ErrorOnFlow", "$e")
                        }
                        .collect { listofTenants ->
                            _state.update {
                                it.copy(
                                    tenants = listofTenants.flatMap { it.tenants },
                                    propertyName = events.propertyName
                                )
                            }

                        }


                }
            }

            is Events.setLocationName -> {
                _state.update {
                    it.copy(
                        locationName = events.locationName
                    )
                }
            }

            is Events.setLocationDescription -> {
                _state.update {
                    it.copy(
                        locationDescription = events.locationDescription
                    )
                }
            }

            is Events.setPropertyName -> {
                _state.update {
                    it.copy(
                        propertyName = events.propertyName
                    )
                }
            }

            is Events.setpropertyDescription -> {
                _state.update {
                    it.copy(
                        propertyDescription = events.propertyDescription
                    )
                }
            }

            is Events.setpropertyAddress -> {
                _state.update {
                    it.copy(
                        propertyAddress = events.propertyAddress
                    )
                }
            }

            is Events.setcapacity -> {
                _state.update {
                    it.copy(
                        capacity = events.capacity
                    )
                }
            }

            is Events.setlocationId -> {
                _state.update {
                    it.copy(
                        locationId = events.locationId
                    )
                }
            }

            is Events.setfullName -> {
                _state.update {
                    it.copy(
                        fullName = events.fullName
                    )
                }
            }

            is Events.setemail -> {
                _state.update {
                    it.copy(
                        email = events.email
                    )
                }
            }

            is Events.setphoneNumber -> {
                _state.update {
                    it.copy(
                        phoneNumber = events.phoneNumber
                    )
                }
            }

            is Events.setpropertyId -> {
                _state.update {
                    it.copy(
                        propertyId = events.propertyId
                    )
                }
            }

            is Events.saveLocation -> {
                val Uri = events.imagesUri
                var locationImages: ArrayList<String> by mutableStateOf(ArrayList<String>())
//                    val locationId = dao.insertLocation(location).toInt()
                val metadata = StorageMetadata.Builder()
                    .setContentType("image/jpeg")
                    .build()

                Log.d("uriList", "$locationImages + what is stored in the firebase ")

                viewModelScope.launch {
                    for (uri in Uri) {

                        val storageRef = storageRef.child("Locations/${uri.hashCode()}.jpeg")
                        if (uri != null) {
                            Log.d("uriList", "$uri + in the loop")
                            storageRef.putFile(uri, metadata)
                                .addOnCompleteListener { storageTask ->
                                    if (storageTask.isSuccessful) {
                                        storageRef.downloadUrl
                                            .addOnSuccessListener { uri ->
                                                _state.update {
                                                    it.copy(
                                                        active = false
                                                    )
                                                }
                                                val map = uri.toString()
                                                locationImages.add(map)
                                                Log.d("uriList", "$map +: the map ")
                                                val locationName = _state.value.locationName
                                                val locationDescription =
                                                    _state.value.locationDescription
                                                Log.d("uriList", "$locationImages ")
                                                Log.d(
                                                    "uriList",
                                                    "size of location images: ${locationImages.size} "
                                                )
                                                Log.d("uriList", "$Uri ")
                                                Log.d("uriList", "size of uri: ${Uri.size} ")

                                                if (locationName.isNotBlank() && locationDescription.isNotBlank() && locationImages.size == Uri.size) {
                                                    Log.d("uriList", "Location being saved ")

                                                    val location = location(
                                                        locationName = locationName,
                                                        description = locationDescription,
                                                        locationImages = locationImages
                                                    )
                                                    runBlocking {
                                                        Log.d("uriList", "Dao operation ")

                                                        dao.insertLocation(location)
                                                    }
                                                    _state.update {
                                                        it.copy(
                                                            locationName = "",
                                                            locationDescription = "",
                                                            isAdding = false,
                                                            active = true
                                                        )
                                                    }
                                                }

                                            }

                                            .addOnFailureListener {
                                                Log.e("FirebaseManeno", it.toString())
                                            }
                                    }
                                }
                        }
                    }


                }


            }

            is Events.saveProperty -> {
                val UriList = events.imagesUri
                val propertyImages by mutableStateOf(ArrayList<String>())
                val metadata = StorageMetadata.Builder()
                    .setContentType("image/jpeg")
                    .build()
                Log.d("uriList", "what enters the viewModel $UriList")
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            active = false
                        )
                    }
                    for (uri in UriList) {
                        val storageRef = storageRef.child("Property/${uri.hashCode()}.jpeg")

                        if (uri != null) {
                            Log.d("uriList", "In the loop: $uri")
                            storageRef.putFile(uri, metadata)
                                .addOnCompleteListener { storageTask ->
                                    if (storageTask.isSuccessful) {
                                        storageRef.downloadUrl
                                            .addOnSuccessListener { uri ->

                                                val map = uri.toString()
                                                propertyImages.add(map)
                                                Log.d("uriList", "$propertyImages ")
                                                Log.d(
                                                    "uriList",
                                                    "size of location images: ${propertyImages.size} "
                                                )
                                                Log.d("uriList", "$UriList ")
                                                Log.d("uriList", "size of uri: ${UriList.size} ")
                                                val propertyName = _state.value.propertyName
                                                val propertyDescription = _state.value.propertyDescription
                                                val capacity = _state.value.capacity
                                                val locationId = _state.value.locationId
                                                val propertyAddress = _state.value.propertyAddress
                                                if (propertyName.isNotBlank() && propertyDescription.isNotBlank() && capacity.isNotBlank() && propertyAddress.isNotBlank() && propertyImages.size == UriList.size) {
                                                    Log.d("uriList", "Entered if")
                                                    val property = property(
                                                        propertyName = propertyName,
                                                        propertyDescription = propertyDescription,
                                                        capacity = capacity,
                                                        locationId = locationId,
                                                        propertyAddress = propertyAddress,
                                                        propertyImages = propertyImages
                                                    )
                                                    runBlocking {
                                                        Log.d("uriList", "Saved")

                                                        dao.insertProperty(property)
                                                    }
                                                    _state.update {
                                                        it.copy(
                                                            propertyName = "",
                                                            propertyDescription = "",
                                                            capacity = "",
                                                            locationId = 0,
                                                            propertyAddress = "",
                                                            isAdding = false,
                                                            active = true
                                                        )
                                                    }

                                                }


                                            }

                                            .addOnFailureListener {
                                                Log.e("FirebaseManeno", it.toString())
                                            }
                                    }
                                }
                        }
                    }

                }





            }

            is Events.saveTenant -> {
                val ImageUri = events.imageUri
                val metadata = StorageMetadata.Builder()
                    .setContentType("image/jpeg")
                    .build()

                viewModelScope.launch{
                    val storageRef = storageRef.child("Tenants/${ImageUri.hashCode()}.jpeg")
                    storageRef.putFile(ImageUri, metadata)
                        .addOnCompleteListener { storageTask ->
                            if (storageTask.isSuccessful) {
                                storageRef.downloadUrl
                                    .addOnSuccessListener { uri ->

                                        val map = uri.toString()
                                        Log.d("uriList", "The $uri being saved ")
                                        val fullName = _state.value.fullName
                                        val email = _state.value.email
                                        val phoneNumber = _state.value.phoneNumber
                                        val propertyId = _state.value.propertyId
                                        val hasPaid = _state.value.hasPaidRent
                                        if (fullName.isNotBlank() && email.isNotBlank() && phoneNumber.isNotBlank()) {
                                            Log.d("uriList", "Entered if")
                                            val tenant = tenant(
                                                fullName = fullName,
                                                email = email,
                                                phoneNumber = phoneNumber,
                                                propertyId = propertyId,
                                                Imageuri = map,
                                                hasPaid = hasPaid
                                            )
                                            runBlocking {
                                                Log.d("uriList", "Saved")
                                                dao.insertTenant(tenant)
                                            }
                                            _state.update {
                                                it.copy(
                                                    fullName = "",
                                                    email = "",
                                                    phoneNumber = "",
                                                    propertyId = 0,
                                                    isAdding = false,
                                                    active = true
                                                )
                                            }

                                        }


                                    }

                                    .addOnFailureListener {
                                        Log.e("FirebaseManeno", it.toString())
                                    }
                            }
                        }

                }

            }

            is Events.Adding -> {
                _state.update {
                    it.copy(
                        isAdding = true
                    )
                }
            }

            is Events.NotAdding -> {
                _state.update {
                    it.copy(
                        isAdding = false
                    )
                }
            }

            is Events.deleteLocation -> {
                viewModelScope.launch {
                    dao.deleteLocation(events.location)
                }
            }

            is Events.deleteProperty -> {
                viewModelScope.launch {
                    dao.deleteProperty(events.property)
                }
            }

            is Events.deleteTenant -> {
                viewModelScope.launch {
                    dao.deleteTenant(events.tenant)
                }
            }

            is Events.confirmRent -> {
                viewModelScope.launch {
                    dao.updateTenant(tenantId = events.tenantId, hasPaid = events.hasPaid)
                }
            }

            is Events.selectTenant -> {
                viewModelScope.launch {
                    val tenant_selected = dao.getTenant(events.tenantId)
                    _state.update {
                        it.copy(
                            selectedtenant = tenant_selected
                        )
                    }
                }
            }

            is Events.searchTenant -> {
                viewModelScope.launch {
                    dao.searchForTenants(events.tenantName)
                        .flowOn(Dispatchers.IO)
                        .collect { tenants ->
                            _state.update {
                                it.copy(
                                    tenants = tenants
                                )
                            }
                        }
                }
            }

            is Events.searchLocation -> {
                viewModelScope.launch {
                    dao.searchForLocations(events.locationName)
                        .flowOn(Dispatchers.IO)
                        .collect { locations ->
                            _state.update {
                                it.copy(
                                    locations = locations
                                )
                            }
                        }
                }
            }

            is Events.searchProperties -> {
                viewModelScope.launch {
                    dao.searchForProperties(events.name)
                        .flowOn(Dispatchers.IO)
                        .collect { properties ->
                            _state.update {
                                it.copy(
                                    property = properties
                                )
                            }
                        }
                }
            }

            is Events.filterPropertiesbyDescription -> {
                viewModelScope.launch {
                    dao.filterPropertiesUsingDescription(events.description, events.locationId)
                        .flowOn(Dispatchers.IO)
                        .collect { properties ->
                            _state.update {
                                it.copy(
                                    property = properties
                                )
                            }
                        }
                }
            }

            is Events.filterPropertiesbyCapacity -> {
                viewModelScope.launch {
                    dao.filterPropertiesUsingCapcity(events.capacity, events.locationId)
                        .flowOn(Dispatchers.IO)
                        .collect { properties ->
                            _state.update {
                                it.copy(
                                    property = properties
                                )
                            }
                        }
                }

            }
//            is Events.IsActive -> {
//                _state.update { it.copy(
//                    active = true
//                ) }
//            }
//            is Events.NotActive -> {
//                _state.update { it.copy(
//                    active = false
//                ) }
//            }

        }
    }
}

