package com.example.lincrideapp.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rides")
data class Ride(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val pickupLocation: String,
    val destinationLocation: String,
    val fare: Double,
    val timestamp: String = "",
    val name: String = "John Doe",
    val car: String = "Toyota Corolla",
    val plateNumber: String = "XYZ-1234"
)

