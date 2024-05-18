package com.anaraya.anaraya.di

import android.content.SharedPreferences
import com.anaraya.anaraya.util.SharedPrefManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPrefModule {

    @Provides
    @Singleton
    fun getSharedPref(sharedPrefManager: SharedPrefManager): SharedPreferences =
        sharedPrefManager.getSharedPref()

}