package com.example.submission2.ui.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.submission2.R
import com.example.submission2.data.local.AppPreferences
import com.example.submission2.data.network.APIUtils
import com.example.submission2.data.network.Result
import com.example.submission2.data.network.response.LoginResponse
import com.example.submission2.databinding.ActivityLoginBinding
import com.example.submission2.ui.ViewModelFactory
import com.example.submission2.ui.view.main.MainActivity
import com.example.submission2.util.Constants
import com.google.gson.Gson
import retrofit2.HttpException


class LoginActivity : AppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private val Context.dataStore by preferencesDataStore(name = Constants.PREFERENCES_NAME)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
        playAnimation()
    }

    private fun setupView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupViewModel() {
        val appPreferences = AppPreferences.getInstance(dataStore)
        loginViewModel =
            ViewModelProvider(
                this@LoginActivity,
                ViewModelFactory(APIUtils.getAPIService(), appPreferences)
            )[LoginViewModel::class.java]
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            if (!binding.edLoginEmail.isError && !binding.edLoginPassword.isError) {
                loginViewModel.login(
                    binding.edLoginEmail.text.toString(),
                    binding.edLoginPassword.text.toString()
                ).observe(this) { result ->
                    if (result != null) {
                        setLoading(result is Result.Loading)

                        when (result) {
                            is Result.Success -> {
                                if (!(result.data.error as Boolean) && result.data.loginData != null) {
                                    result.data.loginData.apply {
                                        if (name != null && userId != null && token != null) {
                                            loginViewModel.saveLoginInfo(name, userId, token)
                                        }
                                    }

                                    Toast.makeText(
                                        this@LoginActivity,
                                        getString(R.string.login_success),
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    startActivity(
                                        Intent(
                                            this@LoginActivity,
                                            MainActivity::class.java
                                        )
                                    )
                                    finish()
                                } else {
                                    Toast.makeText(
                                        this@LoginActivity,
                                        result.data.message ?: getString(R.string.login_error),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            is Result.Error -> {
                                var message: String = getString(R.string.login_error)

                                try {
                                    Gson().fromJson(
                                        (result.error as HttpException).response()?.errorBody()
                                            ?.string(),
                                        LoginResponse::class.java
                                    ).message?.let {
                                        message = it
                                    }
                                } catch (e: Exception) {
                                }

                                Toast.makeText(
                                    this@LoginActivity,
                                    message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(
                    this@LoginActivity,
                    getString(R.string.login_form_error),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(500)
        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val message = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val emailEdit = ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val passwordEdit = ObjectAnimator.ofFloat(binding.edLoginPassword, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(title, message, email, emailEdit, password, passwordEdit, login)
            start()
        }
    }

    private fun setLoading(isLoading: Boolean) {
        binding.apply {
            loginProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            loginButton.isEnabled = !isLoading
            edLoginEmail.isEnabled = !isLoading
            edLoginPassword.isEnabled = !isLoading
        }
    }


}