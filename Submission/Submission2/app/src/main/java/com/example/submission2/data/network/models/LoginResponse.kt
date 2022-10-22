package com.example.submission2.data.network.models

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @field:SerializedName("loginResult")
    val loginData: LoginData? = null,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)
