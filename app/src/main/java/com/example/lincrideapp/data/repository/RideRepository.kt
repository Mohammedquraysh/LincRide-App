package com.example.lincrideapp.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import com.example.lincrideapp.data.model.FareEstimateResponse
import com.example.lincrideapp.data.model.RideRequestResponse
import com.example.lincrideapp.data.room.Ride
import com.example.lincrideapp.data.room.RideDao
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

class RideRepository @Inject constructor(
    private val fareRepository: FareRepository,
    private val rideDao: RideDao
) {
    // Get all rides from the database
    fun getAllRides(): LiveData<List<Ride>> = rideDao.getAllRides()


    // Get fare estimate for each ride
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getFareEstimate(pickup: LatLng, destination: LatLng): FareEstimateResponse {
        return fareRepository.getFareEstimate(pickup, destination)
    }

    suspend fun requestRide(ride : Ride): RideRequestResponse {
        return fareRepository.requestRide(ride)
    }


    // Insert a new ride into the database
    suspend fun insertRide(ride: Ride) {
        rideDao.insert(ride)
    }
}