package com.example.submission2.ui.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
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
import com.example.submission2.data.network.APIUtils
import com.example.submission2.data.network.Result
import com.example.submission2.data.network.models.RegisterResponse
import com.example.submission2.data.local.AppPreferences
import com.example.submission2.databinding.ActivitySignupBinding
import com.example.submission2.util.Constants
import com.example.submission2.ui.ViewModelFactory
import com.google.gson.Gson
import retrofit2.HttpException

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var signupViewModel: SignupViewModel
    private val Context.dataStore by preferencesDataStore(name = Constants.PREFERENCES_NAME)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
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
        signupViewModel =
            ViewModelProvider(
                this@SignupActivity,
                ViewModelFactory(APIUtils.getAPIService(), appPreferences)
            )[SignupViewModel::class.java]
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            if (!binding.nameEditText.isError &&
                !binding.emailEditText.isError &&
                !binding.passwordEditText.isError
            ) {
                signupViewModel.register(
                    binding.nameEditText.text.toString(),
                    binding.emailEditText.text.toString(),
                    binding.passwordEditText.text.toString()
                ).observe(this) { result ->
                    if (result != null) {
                        setLoading(result is Result.Loading)

                        when (result) {
                            is Result.Success -> {
                                if (!(result.data.error as Boolean)) {
                                    Toast.makeText(
                                        this,
                                        getString(R.string.register_success),
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                    finish()
                                } else {
                                    Toast.makeText(
                                        this,
                                        result.data.message ?: getString(R.string.register_error),
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                    finish()
                                }
                            }

                            is Result.Error -> {
                                var message: String = getString(R.string.register_error)

                                try {
                                    Gson().fromJson(
                                        (result.error as HttpException).response()?.errorBody()
                                            ?.string(),
                                        RegisterResponse::class.java
                                    ).message?.let {
                                        message = it
                                    }
                                } catch (e: Exception) {
                                }

                                Toast.makeText(
                                    this@SignupActivity,
                                    message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(
                    this@SignupActivity,
                    getString(R.string.register_form_error),
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

        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(500)
        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val name = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(500)
        val nameEdit = ObjectAnimator.ofFloat(binding.nameEditText, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val emailEdit = ObjectAnimator.ofFloat(binding.emailEditText, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val passwordEdit = ObjectAnimator.ofFloat(binding.passwordEditText, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(title, name, nameEdit, email, emailEdit, password, passwordEdit, signup)
            start()
        }
    }

    private fun setLoading (isLoading: Boolean) {
        binding.apply {
            binding.signupProggress.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.signupButton.isEnabled = !isLoading
            binding.nameEditText.isEnabled = !isLoading
            binding.emailEditText.isEnabled = !isLoading
            binding.passwordEditText.isEnabled = !isLoading
        }
    }
}