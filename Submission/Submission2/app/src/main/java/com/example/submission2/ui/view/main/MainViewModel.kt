package com.example.submission2.ui.view.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.submission2.data.repository.AuthRepository
import com.example.submission2.data.repository.StoryRepository

class MainViewModel(
    private val authRepository: AuthRepository,
    private val storyRepository: StoryRepository
) : ViewModel() {
    fun getBearerToken() = authRepository.getBearerToken()

    fun getMainStories(bearerToken: String) =
        storyRepository.getPagedStories(bearerToken).cachedIn(viewModelScope)

    fun getMapsStories(bearerToken: String, size: Int?) = storyRepository.getStories(
        bearerToken,
        null,
        size,
        Location.LOCATION_ON
    )

    fun logout() = authRepository.logout()
}