package com.example.lincrideapp.data.model

import com.example.lincrideapp.data.room.Ride

data class RideRequestResponse(
    val status: String,
    val ride: Ride,
    val estimated_arrival: String
)
