package com.example.submission2.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.submission2.data.network.APIService
import com.example.submission2.data.repository.AuthRepository
import com.example.submission2.ui.view.create.CreateStoryViewModel
import com.example.submission2.ui.view.login.LoginViewModel
import com.example.submission2.ui.view.main.MainViewModel
import com.example.submission2.ui.view.signup.SignupViewModel
import com.example.submission2.data.repository.StoryRepository
import com.example.submission2.data.local.AppPreferences

class ViewModelFactory(
    apiService: APIService,
    appPreferences: AppPreferences
) : ViewModelProvider.NewInstanceFactory() {
    private val authRepository = AuthRepository(apiService, appPreferences)
    private val storyRepository = StoryRepository(apiService)

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateStoryViewModel::class.java)) {
            return CreateStoryViewModel(authRepository, storyRepository) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(authRepository) as T
        } else if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(authRepository, storyRepository) as T
        } else if (modelClass.isAssignableFrom(SignupViewModel::class.java)) {
            return SignupViewModel(authRepository) as T
        }

        throw IllegalArgumentException(modelClass.name + " not found")
    }
}