package com.example.lincrideapp.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


//@Database(entities = [Ride::class], version = 1)
//abstract class RideDatabase : RoomDatabase() {
//    abstract fun rideDao(): RideDao
//}

@Database(entities = [Ride::class], version = 2)
abstract class RideDatabase : RoomDatabase() {
    abstract fun rideDao(): RideDao

    companion object {
        @Volatile
        private var INSTANCE: RideDatabase? = null

        fun getDatabase(context: Context): RideDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RideDatabase::class.java,
                    "ride_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}