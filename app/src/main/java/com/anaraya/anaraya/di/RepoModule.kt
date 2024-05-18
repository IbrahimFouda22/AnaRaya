package com.anaraya.anaraya.di

import com.anaraya.data.remote.RemoteDataSource
import com.anaraya.data.repo.Repo
import com.anaraya.domain.repo.IRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Provides
    fun provideRepo(remoteDataSource: RemoteDataSource): IRepo {
        return Repo(remoteDataSource)
    }
}