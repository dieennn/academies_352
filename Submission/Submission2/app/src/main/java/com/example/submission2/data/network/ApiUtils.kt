package com.example.submission2.data.network

import com.example.submission2.BuildConfig
import com.example.submission2.util.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APIUtils {
    fun formatBearerToken(token: String): String {
        return "Bearer $token"
    }

    fun getAPIService(): APIService {
        return Retrofit.Builder()
            .baseUrl(Constants.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(
                        if(BuildConfig.DEBUG){
                            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                        } else {
                            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
                        }
                    )
                    .build()
            )
            .build()
            .create(APIService::class.java)
    }
}