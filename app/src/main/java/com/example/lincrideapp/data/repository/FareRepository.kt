package com.example.lincrideapp.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.lincrideapp.data.model.Driver
import com.example.lincrideapp.data.model.FareEstimateResponse
import com.example.lincrideapp.data.model.RideRequestResponse
import com.example.lincrideapp.data.room.Ride
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import java.time.LocalTime
import javax.inject.Inject
import kotlin.random.Random

class FareRepository @Inject constructor() {

    @RequiresApi(Build.VERSION_CODES.O)
    fun getFareEstimate(pickup: LatLng, destination: LatLng): FareEstimateResponse {
        val baseFare = 2.5
        val distanceFare = calculateDistanceFare(pickup, destination)
        val demandMultiplier = calculateDemandMultiplier()
        val trafficMultiplier = simulateTraffic()

        val totalFare = (baseFare + distanceFare) * demandMultiplier * trafficMultiplier
        return FareEstimateResponse(baseFare, distanceFare, demandMultiplier, totalFare)
    }

    fun requestRide(ride : Ride): RideRequestResponse {
        return RideRequestResponse(status = "confirmed", ride = ride, estimated_arrival = "5 min")
    }

    private fun calculateDistanceFare(pickup: LatLng, destination: LatLng): Double {
        val distance = SphericalUtil.computeDistanceBetween(pickup, destination) / 1000 // in km
        return distance * 1.0 // $1/km
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateDemandMultiplier(): Double {
        val currentTime = LocalTime.now()
        return if (currentTime.isAfter(LocalTime.of(7, 0)) && currentTime.isBefore(LocalTime.of(9, 0))) {
            1.5
        } else if (currentTime.isAfter(LocalTime.of(17, 0)) && currentTime.isBefore(LocalTime.of(19, 0))) {
            1.5
        } else {
            1.0
        }
    }

    private fun simulateTraffic(): Double {
        return if (Random.nextBoolean()) 2.0 else 1.0 // Random traffic conditions
    }
}
