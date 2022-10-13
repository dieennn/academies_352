package com.example.submission1.ui.view.detail

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.submission1.R
import com.example.submission1.databinding.ActivityDetailBinding
import com.example.submission1.util.Constants
import com.example.submission1.util.covertTimeToText
import com.example.submission1.util.response.Story
import java.text.SimpleDateFormat
import java.util.*


class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupView()
        detailView()
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

    private fun detailView() {
        val binding = ActivityDetailBinding.inflate(layoutInflater)
        val story = intent.getParcelableExtra<Story>(Constants.INTENT_MAIN_TO_DETAIL)

        setContentView(binding.root)
        story?.let {
            Glide.with(this)
                .load(story.photoUrl)
                .placeholder(R.drawable.ic_baseline_broken_image_24)
                .error(R.drawable.ic_baseline_broken_image_24)
                .into(binding.detailIvPhoto)
            binding.detailIvPhoto.contentDescription = story.description

            binding.detailTvName.text = story.name
            binding.detailTvCreatedAt.text = String.format(
                getString(R.string.created_at_format),
                covertTimeToText(story.createdAt).toString()
            )
            binding.detailTvDescription.text = story.description
            val locationLatLon = "${story.lat ?: "-"}, ${story.lon ?: "-"}"
            binding.detailTvLocation.text = String.format(getString(R.string.location_format), locationLatLon) ?: "-"
        } ?: run {
            Toast.makeText(this, getString(R.string.data_invalid), Toast.LENGTH_SHORT).show()
            finish()
        }
    }

}