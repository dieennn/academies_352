package com.example.submission2.ui.view.signup

import androidx.lifecycle.ViewModel
import com.example.submission2.data.repository.AuthRepository

class SignupViewModel(private val authRepository: AuthRepository) : ViewModel() {
    fun register(
        name: String,
        email: String,
        password: String
    ) = authRepository.register(name, email, password)
}