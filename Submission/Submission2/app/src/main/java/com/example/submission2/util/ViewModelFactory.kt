package com.example.submission2.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.submission2.ui.view.create.CreateStoryViewModel
import com.example.submission2.ui.view.login.LoginViewModel
import com.example.submission2.ui.view.main.MainViewModel
import com.example.submission2.view.signup.SignupViewModel

class ViewModelFactory(private val appPreferences: AppPreferences) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateStoryViewModel::class.java)) {
            return CreateStoryViewModel(appPreferences) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(appPreferences) as T
        } else if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(appPreferences) as T
        } else if (modelClass.isAssignableFrom(SignupViewModel::class.java)) {
            return SignupViewModel(appPreferences) as T
        }

        throw IllegalArgumentException(modelClass.name + " not found")
    }
}