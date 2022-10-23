package com.example.submission2.ui.view.detail

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.submission2.R
import com.example.submission2.data.network.response.Story
import com.example.submission2.databinding.ActivityDetailBinding
import com.example.submission2.util.Constants
import com.example.submission2.util.covertTimeToText
import com.example.submission2.util.formatCreatedAt


class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        detailView()
    }

    private fun detailView() {
        val binding = ActivityDetailBinding.inflate(layoutInflater)
        val story = intent.getParcelableExtra<Story>(Constants.INTENT_MAIN_TO_DETAIL)

        setContentView(binding.root)
        story?.let {
            Glide.with(this)
                .load(story.photoUrl)
                .placeholder(R.drawable.logo)
                .error(R.drawable.ic_baseline_broken_image_24)
                .into(binding.detailIvPhoto)
            binding.detailIvPhoto.contentDescription = story.description

            binding.detailTvName.text = story.name
            binding.detailTvCreatedAt.text = String.format(
                getString(R.string.created_at_format),
                covertTimeToText(formatCreatedAt(story.createdAt.toString())).toString()
            )
            binding.detailTvDescription.text = story.description
            val locationLatLon = "${story.lat ?: "-"}, ${story.lon ?: "-"}"
            binding.detailTvLocation.text =
                String.format(getString(R.string.location_format), locationLatLon)
        } ?: run {
            Toast.makeText(this, getString(R.string.data_invalid), Toast.LENGTH_SHORT).show()
            finish()
        }
    }

}