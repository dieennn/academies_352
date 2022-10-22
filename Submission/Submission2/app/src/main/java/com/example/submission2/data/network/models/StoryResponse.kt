package com.example.submission2.data.network.models

import com.google.gson.annotations.SerializedName

data class StoryResponse(
    @field:SerializedName("listStory")
    val listStory: List<Story>,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)