package com.example.lincrideapp.data.model

data class Driver(
    val name: String,
    val car: String,
    val plateNumber: String
)



//fun requestRide(): RideRequestResponse {
//    val driver = Driver(name = "John Doe", car = "Toyota Prius", plateNumber = "XYZ-1234")
//    return RideRequestResponse(status = "confirmed", driver = driver, estimated_arrival = "5 min")
//}