package com.example.submission1.ui.view.login

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
import com.example.submission1.R
import com.example.submission1.databinding.ActivityLoginBinding
import com.example.submission1.ui.view.main.MainActivity
import com.example.submission1.util.AppPreferences
import com.example.submission1.util.Constants
import com.example.submission1.util.SingleEvent
import com.example.submission1.util.ViewModelFactory
import com.example.submission1.util.response.LoginResponse

private val Context.dataStore by preferencesDataStore(name = Constants.PREFERENCES_NAME)

class LoginActivity : AppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

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
        @Suppress("DEPRECATION")
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
                ViewModelFactory(appPreferences)
            )[LoginViewModel::class.java]
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            if (!binding.edLoginEmail.isError && !binding.edLoginPassword.isError) {
                loginViewModel.login(
                    binding.edLoginEmail.text.toString(),
                    binding.edLoginPassword.text.toString()
                )
            } else {
                Toast.makeText(
                    this@LoginActivity,
                    getString(R.string.login_form_error),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        loginViewModel.getLoginData().observe(this@LoginActivity) { loginData: LoginResponse ->
            if (!(loginData.error as Boolean)) {
                Toast.makeText(
                    this@LoginActivity,
                    getString(R.string.login_success),
                    Toast.LENGTH_SHORT
                ).show()
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        }

        loginViewModel.getLoginError()
            .observe(this@LoginActivity) { loginError: SingleEvent<String> ->
                loginError.getData()?.let {
                    Toast.makeText(this@LoginActivity, it, Toast.LENGTH_SHORT).show()
                }
            }

        loginViewModel.isLoading().observe(this@LoginActivity) { isLoading: Boolean ->
            binding.apply {
                loginProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                loginButton.isEnabled = !isLoading
                edLoginEmail.isEnabled = !isLoading
                edLoginPassword.isEnabled = !isLoading
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

}