package com.example.submission1.ui.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submission1.service.*
import com.example.submission1.util.AppPreferences
import com.example.submission1.util.SingleEvent
import com.example.submission1.util.response.LoginResponse
import com.example.submission1.util.response.ReadStoryResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val appPreferences: AppPreferences) : ViewModel() {
    private val storiesData: MutableLiveData<ReadStoryResponse> = MutableLiveData()
    private val storiesError: MutableLiveData<SingleEvent<String>> = MutableLiveData()
    private val isLoading: MutableLiveData<Boolean> = MutableLiveData()

    fun getStoriesData(): LiveData<ReadStoryResponse> {
        return storiesData
    }

    fun getStoriesError(): LiveData<SingleEvent<String>> {
        return storiesError
    }

    fun isLoading(): LiveData<Boolean> {
        return isLoading
    }

    fun loadStories(page: Int?, size: Int?, location: Location) {
        isLoading.value = true

        var bearerToken: String

        runBlocking {
            bearerToken = appPreferences.getTokenPrefs().first() ?: ""
        }

        getAPIService().getStories(
            formatBearerToken(bearerToken),
            page,
            size,
            location.isOn
        ).enqueue(object : Callback<ReadStoryResponse> {
            override fun onResponse(call: Call<ReadStoryResponse>, response: Response<ReadStoryResponse>) {
                isLoading.value = false

                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.error as Boolean) {
                            storiesError.value = SingleEvent(it.message as String)
                        } else {
                            storiesData.value = response.body() as ReadStoryResponse
                        }
                    } ?: run {
                        storiesError.value = SingleEvent(UNEXPECTED_DATA_ERROR)
                    }
                } else {
                    val body: LoginResponse? =
                        Gson().fromJson(response.errorBody()?.string(), LoginResponse::class.java)

                    storiesError.value =
                        SingleEvent(body?.message ?: formatResponseCode(response.code()))
                }
            }

            override fun onFailure(call: Call<ReadStoryResponse>, t: Throwable) {
                isLoading.value = false
                storiesError.value = SingleEvent(UNEXPECTED_ERROR)
            }
        })
    }

    fun logout() {
        viewModelScope.launch {
            appPreferences.clearPrefs()
        }
    }
}

enum class Location(val isOn: Int) {
    LOCATION_ON(1),
    LOCATION_OFF(0)
}