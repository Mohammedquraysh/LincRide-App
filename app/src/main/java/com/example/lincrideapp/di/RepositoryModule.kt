package com.example.lincrideapp.di

import com.example.lincrideapp.data.repository.FareRepository
import com.example.lincrideapp.data.room.RideDao
import com.example.lincrideapp.data.repository.RideRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRideRepository(rideDao: RideDao,fareRepository: FareRepository): RideRepository {
        return RideRepository(fareRepository, rideDao)
    }
}