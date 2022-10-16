package com.example.submission1.ui.view.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.preferencesDataStore
import com.example.submission1.databinding.ActivitySplashBinding
import com.example.submission1.ui.view.main.MainActivity
import com.example.submission1.ui.view.welcome.WelcomeActivity
import com.example.submission1.util.AppPreferences
import com.example.submission1.util.Constants
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class SplashActivity : AppCompatActivity() {
    private val Context.dataStore by preferencesDataStore(name = Constants.PREFERENCES_NAME)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val preferences = AppPreferences.getInstance(dataStore)

        var token: String?

        runBlocking {
            token = preferences.getTokenPrefs().first()
        }

        Handler(Looper.getMainLooper()).postDelayed({
            if (token == null) {
                startActivity(Intent(this, WelcomeActivity::class.java))
            } else {
                startActivity(Intent(this, MainActivity::class.java))
            }

            finish()
        }, Constants.SPLASH_SCREEN_DELAY)
    }
}