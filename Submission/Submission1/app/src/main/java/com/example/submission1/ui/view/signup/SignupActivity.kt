package com.example.submission1.ui.view.signup

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
import com.example.submission1.R
import com.example.submission1.databinding.ActivitySignupBinding
import com.example.submission1.util.AppPreferences
import com.example.submission1.util.Constants
import com.example.submission1.util.ViewModelFactory
import com.example.submission1.view.signup.SignupViewModel

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
//        signupViewModel = ViewModelProvider(
//            this,
//            ViewModelFactory(UserPreference.getInstance(dataStore))
//        )[SignupViewModel::class.java]
        val appPreferences = AppPreferences.getInstance(dataStore)
        signupViewModel =
            ViewModelProvider(
                this@SignupActivity,
                ViewModelFactory(appPreferences)
            )[SignupViewModel::class.java]
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
//            val name = binding.nameEditText.text.toString()
//            val email = binding.emailEditText.text.toString()
//            val password = binding.passwordEditText.text.toString()
//            when {
//                name.isEmpty() -> {
//                    binding.nameEditText.error = "Masukkan nama"
//                }
//                email.isEmpty() -> {
//                    binding.emailEditText.error = "Masukkan email"
//                }
//                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
//                    binding.emailEditText.error = "Format email salah"
//                }
//                password.isEmpty() -> {
//                    binding.passwordEditText.error = "Masukkan password"
//                }
//                else -> {
////                    signupViewModel.saveUser(UserModel(name, email, password, false))
////                    AlertDialog.Builder(this).apply {
////                        setTitle("Yeah!")
////                        setMessage("Akunnya sudah jadi nih. Yuk, login dan belajar coding.")
////                        setPositiveButton("Lanjut") { _, _ ->
////                            finish()
////                        }
////                        create()
////                        show()
////                    }
//                    signupViewModel.register(
//                        name, email, password
//                    )
//                }
//            }

            if (!binding.nameEditText.isError &&
                !binding.emailEditText.isError &&
                !binding.passwordEditText.isError
            ) {
                signupViewModel.register(
                    binding.nameEditText.text.toString(),
                    binding.emailEditText.text.toString(),
                    binding.passwordEditText.text.toString()
                )
            } else {
                Toast.makeText(
                    this@SignupActivity,
                    getString(R.string.register_form_error),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        signupViewModel.getRegisterData().observe(this@SignupActivity) { registerResponse ->
            if (!(registerResponse.error as Boolean)) {
                Toast.makeText(this, getString(R.string.register_success), Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
        }

        signupViewModel.getRegisterError().observe(this@SignupActivity) { registerError ->
            registerError.getData()?.let {
                Toast.makeText(this@SignupActivity, it, Toast.LENGTH_SHORT).show()
            }
        }

        signupViewModel.isLoading().observe(this@SignupActivity) { isLoading: Boolean ->
            binding.signupProggress.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.signupButton.isEnabled = !isLoading
            binding.nameEditText.isEnabled = !isLoading
            binding.emailEditText.isEnabled = !isLoading
            binding.passwordEditText.isEnabled = !isLoading
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
}