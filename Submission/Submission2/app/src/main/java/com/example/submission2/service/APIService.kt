package com.example.submission2.service

import com.example.submission2.util.Constants
import com.example.submission2.util.response.CreateStoryResponse
import com.example.submission2.util.response.LoginResponse
import com.example.submission2.util.response.ReadStoryResponse
import com.example.submission2.util.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

const val UNEXPECTED_ERROR = "Unexpected Error"
const val UNEXPECTED_DATA_ERROR = "Unexpected Data Error"

interface APIService {
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @Multipart
    @POST("stories")
    fun postStory(
        @Header("Authorization") authorization: String,
        @Part("description") description: RequestBody,
        @Part photo: MultipartBody.Part,
        @Part("lat") lat: RequestBody?,
        @Part("lon") lon: RequestBody?
    ): Call<CreateStoryResponse>

    @GET("stories")
    fun getStories(
        @Header("Authorization") authorization: String,
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        @Query("location") location: Int?
    ): Call<ReadStoryResponse>
}

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
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                )
                .build()
        )
        .build()
        .create(APIService::class.java)
}

fun formatResponseCode(code: Int): String {
    return if (code >= 500) {
        "Server Error"
    } else if (code >= 400) {
        "Client Error"
    } else {
        UNEXPECTED_ERROR
    }
}