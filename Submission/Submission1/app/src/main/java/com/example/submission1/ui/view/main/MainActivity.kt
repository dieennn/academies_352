package com.example.submission1.ui.view.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission1.R
import com.example.submission1.adapter.StoryListAdapter
import com.example.submission1.databinding.ActivityMainBinding
import com.example.submission1.ui.view.create.CreateStoryActivity
import com.example.submission1.ui.view.welcome.WelcomeActivity
import com.example.submission1.util.AppPreferences
import com.example.submission1.util.Constants
import com.example.submission1.util.ViewModelFactory

class MainActivity : AppCompatActivity() {
    companion object {
        const val REQUEST_PERMISSION_CODE = 1111
        const val INTENT_CREATE_STORY = 2222
    }

    private val Context.dataStore by preferencesDataStore(name = Constants.PREFERENCES_NAME)

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private val storyListAdapter = StoryListAdapter()

    private val intentCreateStoryResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == INTENT_CREATE_STORY) {
                storyListAdapter.submitList(listOf())
                mainViewModel.loadStories(null, null, Location.LOCATION_OFF)
            }
        }

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
    }

    private fun setupViewModel() {
        val appPreferences = AppPreferences.getInstance(dataStore)
        mainViewModel =
            ViewModelProvider(
                this@MainActivity,
                ViewModelFactory(appPreferences)
            )[MainViewModel::class.java]

        binding.mainRvStories.adapter = storyListAdapter

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.mainRvStories.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.mainRvStories.layoutManager = LinearLayoutManager(this)
        }

        binding.mainSwipeRefreshLayout.setOnRefreshListener {
            storyListAdapter.submitList(listOf())
            mainViewModel.loadStories(null, null, Location.LOCATION_OFF)
        }

        binding.mainFabCreate.setOnClickListener {
            if (!isPermissionGranted(Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    REQUEST_PERMISSION_CODE
                )
            } else {
                intentCreateStoryResult.launch(Intent(this, CreateStoryActivity::class.java))
            }
        }

        mainViewModel.isLoading().observe(this) { isLoading ->
            binding.mainSwipeRefreshLayout.isRefreshing = isLoading
        }

        mainViewModel.getStoriesData().observe(this) { storiesData ->
            storyListAdapter.submitList(storiesData.listStory)
        }

        mainViewModel.getStoriesError().observe(this) { storiesError ->
            storiesError.getData()?.let {
                Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
            }
        }

        Handler(Looper.getMainLooper()).postDelayed({
            mainViewModel.loadStories(null, null, Location.LOCATION_OFF)
        }, 250)
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
}