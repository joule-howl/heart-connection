package com.example.deviceinfosharing.di

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt Module for SharedPreferences and Gson Dependencies
 * Provides singleton instances of SharedPreferences and Gson for dependency injection
 */
@Module
@InstallIn(SingletonComponent::class)
object SharedPreferencesModule {

    private const val PREFERENCES_NAME = "device_info_sharing_prefs"

    /**
     * Provides a singleton instance of SharedPreferences
     * @param context The application context
     * @return SharedPreferences instance
     */
    @Singleton
    @Provides
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    /**
     * Provides a singleton instance of Gson
     * @return Gson instance for JSON serialization/deserialization
     */
    @Singleton
    @Provides
    fun provideGson(): Gson {
        return Gson()
    }
}
