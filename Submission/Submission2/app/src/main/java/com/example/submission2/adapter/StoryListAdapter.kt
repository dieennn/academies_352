package com.example.submission2.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submission2.R
import com.example.submission2.databinding.ItemStoryBinding
import com.example.submission2.ui.view.detail.DetailActivity
import com.example.submission2.util.Constants
import com.example.submission2.util.covertTimeToText
import com.example.submission2.util.formatCreatedAt
import com.example.submission2.util.response.Story

class StoryListAdapter :
    ListAdapter<Story, StoryListAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            ItemStoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(holder.adapterPosition)
        val context = holder.itemView.context


        Glide.with(holder.itemView.context)
            .load(data.photoUrl)
            .placeholder(R.drawable.logo)
            .error(R.drawable.ic_baseline_broken_image_24)
            .into(holder.binding.itemStoryIvImg)
        holder.binding.itemStoryIvImg.contentDescription = data.description

        holder.binding.itemStoryTvName.text = data.name
        holder.binding.itemStoryTvCreatedAt.text = String.format(
            holder.itemView.context.getString(R.string.created_at_format),
            covertTimeToText(formatCreatedAt(data.createdAt.toString())).toString()
        )

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(Constants.INTENT_MAIN_TO_DETAIL, data)
            context.startActivity(
                intent,
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    context as Activity,
                    Pair(
                        holder.binding.itemStoryIvImg,
                        context.getString(R.string.transition_detail_photo)
                    ),
                    Pair(
                        holder.binding.itemStoryTvName,
                        context.getString(R.string.transition_detail_name)
                    ),
                    Pair(
                        holder.binding.itemStoryTvCreatedAt,
                        context.getString(R.string.transition_detail_created_at)
                    ),
                ).toBundle()
            )
        }
    }

    inner class ViewHolder(val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id?.equals(newItem.id) as Boolean
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }
        }
    }
}
