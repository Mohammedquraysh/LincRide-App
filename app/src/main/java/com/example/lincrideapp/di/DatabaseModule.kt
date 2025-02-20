package com.example.lincrideapp.di

import android.content.Context
import com.example.lincrideapp.data.room.RideDao
import com.example.lincrideapp.data.room.RideDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideRideDatabase(@ApplicationContext context: Context): RideDatabase {
        return RideDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideRideDao(database: RideDatabase): RideDao {
        return database.rideDao()
    }
}