package com.anaraya.anaraya.util

import android.content.SharedPreferences
import android.util.Log
import com.anaraya.data.remote.ApiService
import dagger.Lazy
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class RayaInterceptor @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val apiService: Lazy<ApiService>
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = sharedPreferences.getString("token", null)
        val request = chain.request().newBuilder()
            .header("Accept-Language", sharedPreferences.getString("lang", "en")!!)
            .header("Authorization", "Bearer $token")
            .build()

        val response = chain.proceed(request)
        if (response.code == 401) {
            synchronized(this) {
                val newToken = refreshAccessToken()
                return if (newToken != null) {
                    val newRequest = chain.request().newBuilder()
                        .header("Accept-Language", sharedPreferences.getString("lang", "en")!!)
                        .header("Authorization", "Bearer $newToken")
                        .build()
                    chain.proceed(newRequest)
                } else {
                    response
                }
            }
        }

        return response
    }
    private fun refreshAccessToken(): String? {
        val refreshToken = sharedPreferences.getString("refreshToken", null) ?: return null
        return runBlocking {
            try {
                val response = apiService.get().refreshToken(refreshToken = "\"$refreshToken\"")
                if (response.isSuccessful) {
                    val body = response.body()
                    sharedPreferences.edit().putString("token", body?.data?.accessToken).apply()
                    sharedPreferences.edit().putString("refreshToken", body?.data?.refreshToken).apply()
                    if (body?.data?.isDeleted == true) {
                        TokenRefreshState.setDeleted(true)
                    }
                    body?.data?.accessToken
                }
                else {
                    if (response.code() == 402) {
//                        val errorBody = response.errorBody()?.string()
//                        if (errorBody != null && errorBody.contains("isDeleted:true")) {
                            TokenRefreshState.setDeleted(true)
//                        }
                    }
//                    sharedPreferences.edit().remove("token").remove("refreshToken").apply()
                }
                    null
            } catch (e: Exception) {
                null
            }
        }
    }
}
