package com.example.submission2

import com.example.submission2.data.network.response.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

object DummyDataGenerator {
    fun generateDummyBearerToken() = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ6.eyJ1c2VySWQiOiJ1c2VyLXlrZ0JWTUhGOFN5a0tMQ1QiLCJpYXQiOjE2NjU2NTI3ODh6.1JOTtdgHYJGDjlofESRNBJhcdAVCAJwsampHfCw_aDI"

    fun generateDummyDescription() = "description".toRequestBody("text/plain".toMediaType())

    fun generateDummyException() = Exception("exception")

    fun generateDummyFileMultipart() = MultipartBody.Part.createFormData(
        "photo",
        "filename",
        File("").asRequestBody("image/jpeg".toMediaType())
    )

    fun generateDummySuccessCreateStoryResponse() =
        CreateStoryResponse(error = false, message = "Submit success")

    fun generateDummySuccessLoginResponse() = LoginResponse(
        error = false,
        message = "Submit success",
        loginData = LoginData(name = "name", userId = "123", token = "abc123")
    )

    fun generateDummySuccessRegisterResponse() = RegisterResponse(
        error = false,
        message = "Register success"
    )

    fun generateDummySuccessStoryResponse() =
        StoryResponse(error = false, message = "success", listStory = generateDummyStories())

    fun generateDummyStories(): List<Story> {
        val stories: MutableList<Story> = arrayListOf()

        for (i in 1..50) {
            stories.add(
                Story(
                    id = "story-$i",
                    name = "John Doe ($i)",
                    description = "Lorem Ipsum ($i)",
                    photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png",
                    createdAt = "2022-10-23T10:35:15.152Z",
                    lat = -6.0019502,
                    lon = 106.0662807
                )
            )
        }

        return stories
    }
}