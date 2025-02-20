package com.example.lincrideapp.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface RideDao {
    @Insert
    suspend fun insert(ride: Ride)

    @Query("SELECT * FROM rides ORDER BY timestamp DESC")
    fun getAllRides(): LiveData<List<Ride>>
}
