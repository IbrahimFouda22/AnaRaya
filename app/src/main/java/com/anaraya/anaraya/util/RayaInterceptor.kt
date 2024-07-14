package com.anaraya.anaraya.util

import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.Response

class RayaInterceptor(private val sharedPreferences: SharedPreferences) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = sharedPreferences.getString("token", null)
        return if (token.isNullOrEmpty()) {
            val request = chain.request().newBuilder().header(
                "Accept-Language",
                sharedPreferences.getString("lang", "en")!!
            )
                .header("Authorization", "Bearer ").build()
            chain.proceed(request)
        } else {
            val request = chain.request().newBuilder().header(
                "Accept-Language",
                sharedPreferences.getString("lang", "en")!!
            )
                .header("Authorization", "Bearer $token").build()
            chain.proceed(request)
        }
    }
}