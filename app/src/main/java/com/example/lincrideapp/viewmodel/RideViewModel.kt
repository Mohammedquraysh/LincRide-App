package com.example.lincrideapp.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lincrideapp.data.model.FareEstimateResponse
import com.example.lincrideapp.data.model.RideRequestResponse
import com.example.lincrideapp.data.room.Ride
import com.example.lincrideapp.data.repository.RideRepository
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RideViewModel @Inject constructor(
    private val rideRepository: RideRepository
) : ViewModel() {

    // LiveData for ride history
    val rideHistory: LiveData<List<Ride>> = rideRepository.getAllRides()


    private val _fareEstimate = MutableLiveData<FareEstimateResponse>()
    val fareEstimate: LiveData<FareEstimateResponse> get() = _fareEstimate

    private val _rideRequest = MutableLiveData<RideRequestResponse>()
    val rideRequest: LiveData<RideRequestResponse> get() = _rideRequest

    @RequiresApi(Build.VERSION_CODES.O)
    fun getFareEstimate(pickup: LatLng, destination: LatLng) {
        viewModelScope.launch {
            _fareEstimate.value = rideRepository.getFareEstimate(pickup, destination)
        }
    }
    // Function to request and insert a new ride
    fun requestRide(ride: Ride) {
        viewModelScope.launch {
            _rideRequest.value = rideRepository.requestRide(ride)
            rideRepository.insertRide(ride)
        }
    }

}