package com.example.submission2.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.submission2.R
import com.example.submission2.databinding.ItemStoryLoadingBinding
import com.example.submission2.ui.OnPagingError

class StoryFooterLoadingAdapter(
    private val onPagingError: OnPagingError
) :
    LoadStateAdapter<StoryFooterLoadingAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.binding.itemStoryLoadingProgressBar.isVisible = loadState is LoadState.Loading

        if (loadState is LoadState.Error) {
            onPagingError.onError(
                loadState.error.localizedMessage
                    ?: holder.itemView.context.getString(R.string.error_data)
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        return ViewHolder(
            ItemStoryLoadingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    class ViewHolder(val binding: ItemStoryLoadingBinding) : RecyclerView.ViewHolder(binding.root)
}