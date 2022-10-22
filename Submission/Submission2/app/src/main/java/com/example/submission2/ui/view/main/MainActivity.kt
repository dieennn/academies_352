package com.example.submission2.ui.view.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.submission2.R
import com.example.submission2.data.network.APIUtils
import com.example.submission2.data.local.AppPreferences
import com.example.submission2.databinding.ActivityMainBinding
import com.example.submission2.ui.view.welcome.WelcomeActivity
import com.example.submission2.util.Constants
import com.example.submission2.ui.ViewModelFactory

class MainActivity : AppCompatActivity() {
    companion object {
        const val REQUEST_PERMISSION_CODE = 1111
        const val INTENT_CREATE_STORY = 2222
    }

    private val Context.dataStore by preferencesDataStore(name = Constants.PREFERENCES_NAME)

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!isPermissionGranted(Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_PERMISSION_CODE
            )
        }

        setupViewModel()

        setupWithNavController(
            binding.mainBottomNavView,
            (supportFragmentManager.findFragmentById(R.id.main_nav_host) as NavHostFragment).navController
        )
    }

    private fun setupViewModel() {
        val appPreferences = AppPreferences.getInstance(dataStore)
        mainViewModel =
            ViewModelProvider(
                this@MainActivity,
                ViewModelFactory(APIUtils.getAPIService(), appPreferences)
            )[MainViewModel::class.java]
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_main_logout) {
            mainViewModel.logout()

            Toast.makeText(
                this@MainActivity,
                getString(R.string.logout_success),
                Toast.LENGTH_SHORT
            ).show()
            startActivity(Intent(this@MainActivity, WelcomeActivity::class.java))
            finish()
        }

        if(item.itemId == R.id.menu_main_change_languange) {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (isPermissionGranted(Manifest.permission.CAMERA)) {
                Toast.makeText(this, getString(R.string.camera_granted), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, getString(R.string.camera_not_granted), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun isPermissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            val setIntent = Intent(Intent.ACTION_MAIN)
            setIntent.addCategory(Intent.CATEGORY_HOME)
            setIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            finish()
            startActivity(setIntent)
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, getString(R.string.twice_back), Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }
}