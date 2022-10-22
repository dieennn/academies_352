package com.example.submission2.data.network

import com.example.submission2.data.network.models.CreateStoryResponse
import com.example.submission2.data.network.models.LoginResponse
import com.example.submission2.data.network.models.RegisterResponse
import com.example.submission2.data.network.models.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface APIService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @Multipart
    @POST("stories")
    suspend fun postStory(
        @Header("Authorization") authorization: String,
        @Part("description") description: RequestBody,
        @Part photo: MultipartBody.Part,
        @Part("lat") lat: RequestBody?,
        @Part("lon") lon: RequestBody?
    ): CreateStoryResponse

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") authorization: String,
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        @Query("location") location: Int?
    ): StoryResponse
}