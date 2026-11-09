package com.example.deviceinfosharing.di

import com.example.deviceinfosharing.data.repository.FirebaseManagerImpl
import com.example.deviceinfosharing.domain.repository.FirebaseManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt Module for Firebase Dependencies
 * Provides singleton instances of Firebase services
 */
@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    /**
     * Provides a singleton instance of FirebaseAuth
     * for authentication operations
     */
    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    /**
     * Provides a singleton instance of FirebaseDatabase
     * for real-time database operations
     */
    @Singleton
    @Provides
    fun provideFirebaseDatabase(): FirebaseDatabase {
        return Firebase.database
    }
    
    /**
     * Provides a singleton instance of FirebaseManager
     * for managing device data operations
     */
    @Singleton
    @Provides
    fun provideFirebaseManager(
        database: FirebaseDatabase
    ): FirebaseManager {
        return FirebaseManagerImpl(database)
    }
}
