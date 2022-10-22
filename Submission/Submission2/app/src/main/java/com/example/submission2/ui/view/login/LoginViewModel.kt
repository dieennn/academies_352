package com.example.submission2.ui.view.login

import androidx.lifecycle.ViewModel
import com.example.submission2.data.repository.AuthRepository

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {
    fun login(email: String, password: String) = authRepository.login(email, password)

    fun saveLoginInfo(name: String, userId: String, token: String) =
        authRepository.saveLoginInfo(name, userId, token)
}