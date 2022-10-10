package com.example.submission1.util

import android.content.Context
import android.graphics.Bitmap
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import com.example.submission1.R

fun getPlaceholderImage(context: Context): Bitmap? {
    return AppCompatResources.getDrawable(context, R.drawable.ic_baseline_broken_image_24)
        ?.toBitmap(256, 256)
}