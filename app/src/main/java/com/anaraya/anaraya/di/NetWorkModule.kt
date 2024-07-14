package com.anaraya.anaraya.di

import android.content.SharedPreferences
import com.anaraya.anaraya.BuildConfig
import com.anaraya.anaraya.util.RayaInterceptor
import com.anaraya.data.remote.ApiService
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetWorkModule {

    @Provides
    @Singleton
    fun provideHttpInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun provideInterceptor(sharedPreferences: SharedPreferences): RayaInterceptor {
        return RayaInterceptor(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideOkHttp(
        interceptor: RayaInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder().connectTimeout(600, TimeUnit.SECONDS).addInterceptor(
            httpLoggingInterceptor
        )
            .readTimeout(600, TimeUnit.SECONDS).addInterceptor(interceptor).build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().client(okHttpClient).addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().disableHtmlEscaping().setLenient().create())).baseUrl(
                BuildConfig.BASE_URL
            ).build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}