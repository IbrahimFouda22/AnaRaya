package com.anaraya.anaraya.di

import com.anaraya.data.remote.IRemoteDataSource
import com.anaraya.data.remote.RemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteDataSourceModule {

    @Binds
    abstract fun bindRemoteDataSource(remoteDataSource: RemoteDataSource): IRemoteDataSource
}