package com.anaraya.anaraya.di

import android.content.SharedPreferences
import com.anaraya.anaraya.util.RayaInterceptor
import com.anaraya.data.remote.ApiService
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InterceptorModule {
    @Provides
    @Singleton
    fun provideInterceptor(sharedPreferences: SharedPreferences,apiService: Lazy<ApiService>): RayaInterceptor {
        return RayaInterceptor(sharedPreferences,apiService)
    }
}