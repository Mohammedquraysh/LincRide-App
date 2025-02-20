package com.example.lincrideapp.data.model

data class FareEstimateResponse(
    val base_fare: Double,
    val distance_fare: Double,
    val demand_multiplier: Double,
    val total_fare: Double
)