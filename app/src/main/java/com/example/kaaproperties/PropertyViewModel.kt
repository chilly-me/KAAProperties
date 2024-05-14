package com.example.kaaproperties

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kaaproperties.logic.Events
import com.example.kaaproperties.logic.locationsData
import com.example.kaaproperties.room.dao.PropertyDao
import com.example.kaaproperties.room.entities.location
import com.example.kaaproperties.room.entities.property
import com.example.kaaproperties.logic.states
import com.example.kaaproperties.room.entities.tenant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import okhttp3.internal.notify
import okhttp3.internal.wait

class PropertyViewModel(
    private val dao: PropertyDao,
    private var auth: FirebaseAuth,
    private var firebaseFirestore: FirebaseFirestore,
    private var storageRef: StorageReference,
    private var context: Context,
) : ViewModel() {

    var loading = mutableStateOf(false)

    fun isLoading(){
        loading.value = true
    }
    fun isNotLoading(){
        loading.value = false
    }


    private val _state = MutableStateFlow(states())
    val state = _state



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

            is Events.setUri -> {
                _state.update {
                    it.copy(
                        uri = events.uri
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
                val locationName = _state.value.locationName
                val locationDescription = _state.value.locationDescription
                if (locationName.isBlank() || locationDescription.isBlank()) {
                    return
                }
                val location = location(
                    locationName = locationName,
                    description = locationDescription
                )


                val uri = events.imagesUri
                Log.d("uriList", "the first Uri in viewModel $uri")
                var locationImages: List<String> by mutableStateOf(emptyList())
                var number = 0

                viewModelScope.launch {
//                    dao.insertLocation(location)
                    val locationId = dao.insertLocation(location).toInt()
                    Log.d("locationID", "${locationId.toInt()}")
                    val metadata = StorageMetadata.Builder()
                        .setContentType("image/jpeg")
                        .build()

                    val job = viewModelScope.launch { Log.d("uriList", "$number The Looping Number")
                        uri.forEach {
                            number = number + 1
                            val storageRef = storageRef.child("Locations/${number}")
                            Log.d("uriList", "$number The Looping Number")
                            if (it != null) {
                                Log.d("uriList", "$it + in the loop")
                                storageRef.putFile(it, metadata)
                                    .addOnCompleteListener { storageTask ->
                                        if (storageTask.isSuccessful) {
                                            storageRef.downloadUrl
                                                .addOnSuccessListener { uri ->
                                                    val map = uri.toString()
                                                    locationImages = locationImages + map
                                                    Log.d("uriList", "$map +: the map ")
                                                    Log.d(
                                                        "uriList",
                                                        "$locationImages +: The updated list of strings"
                                                    )
                                                    Log.d(
                                                        "FirebaseManeno",
                                                        "Successful in adding images to firebase"
                                                    )

                                                }

                                                .addOnFailureListener {
                                                    Log.e("FirebaseManeno", it.toString())
                                                }
                                        }
                                    }
                            }
                        }
                        val locations = locationsData(
                            locationId = locationId,
                            locationImages = locationImages
                        )
                        Log.d("uriList", "$locationImages + what is stored in the firebase ")
                        Log.d("uriList", "$locationId the locationId stored in firestore  ")
                    }


                    val dblocations: DocumentReference = firebaseFirestore.collection("Locations").document(locationId.toString())
                    dblocations.set(locations)
                        .addOnCompleteListener { savingtoFireStore ->
                            if (savingtoFireStore.isSuccessful) {
                                Toast.makeText(
                                    context,
                                    "Added location profile successfully",
                                    Toast.LENGTH_SHORT,

                                    ).show()
                                _state.update {it.copy(
                                    isAdding = false
                                )
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    savingtoFireStore.exception.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        }







                }
                _state.update {
                    it.copy(
                        locationName = "",
                        locationDescription = ""
                    )
                }
            }

            is Events.saveProperty -> {
                val propertyName = _state.value.propertyName
                val propertyDescription = _state.value.propertyDescription
                val capacity = _state.value.capacity
                val locationId = _state.value.locationId
                val propertyAddress = _state.value.propertyAddress
                if (propertyName.isBlank() || propertyDescription.isBlank() || capacity.isBlank() || propertyAddress.isBlank()) {
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
                _state.update {
                    it.copy(
                        propertyName = "",
                        propertyDescription = "",
                        capacity = "",
                        locationId = 0,
                        propertyAddress = ""
                    )
                }

            }

            is Events.saveTenant -> {
                val fullName = _state.value.fullName
                val email = _state.value.email
                val phoneNumber = _state.value.phoneNumber
                val propertyId = _state.value.propertyId
                val hasPaid = _state.value.hasPaidRent
                val uri = _state.value.uri

                val tenant = tenant(
                    fullName = fullName,
                    email = email,
                    phoneNumber = phoneNumber,
                    propertyId = propertyId,
                    uri = uri,
                    hasPaid = hasPaid
                )

                viewModelScope.launch {
                    dao.insertTenant(tenant)
                }
                _state.update {
                    it.copy(
                        fullName = "",
                        email = "",
                        phoneNumber = "",
                        propertyId = 0
                    )
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

        }
    }
}