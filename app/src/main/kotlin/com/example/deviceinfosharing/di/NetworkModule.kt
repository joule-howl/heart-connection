package com.example.deviceinfosharing.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import javax.inject.Singleton

/**
 * Hilt Module for Network Dependencies
 * Provides singleton instances of network-related services
 * including Retrofit client and HTTP interceptors
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val WEATHER_API_BASE_URL = "https://api.openweathermap.org/data/2.5/"

    /**
     * Provides a singleton instance of Gson for JSON serialization/deserialization
     */
    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    /**
     * Provides a singleton instance of HttpLoggingInterceptor
     * for logging HTTP requests and responses
     */
    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { message ->
            Timber.d("HTTP: $message")
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    /**
     * Provides a singleton instance of OkHttpClient
     * configured with logging interceptor and timeout settings
     */
    @Singleton
    @Provides
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .build()
    }

    /**
     * Provides a singleton instance of Retrofit
     * configured with OkHttpClient and Gson converter factory
     */
    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(WEATHER_API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}
