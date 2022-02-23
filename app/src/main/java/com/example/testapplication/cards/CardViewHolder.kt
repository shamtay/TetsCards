package com.example.testapplication.cards

import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testapplication.R
import com.example.testapplication.data.api.model.GithubRepositoryItem
import com.example.testapplication.databinding.ItemCardBinding
import kotlin.math.min

class CardViewHolder(private val binding: ItemCardBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(itemData: GithubRepositoryItem, position: Int) {
        binding.repoName.text = itemData.name

        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            binding.root.context.resources.getDimensionPixelSize(R.dimen.card_size)
        )

        if (position == 1) {
            params.topMargin =
                -binding.root.context.resources.getDimensionPixelSize(R.dimen.overlapping_area)

        } else {
            params.topMargin = 0
        }
        binding.root.rotation = 0f
        binding.root.layoutParams = params
        binding.root.x = 0f
        binding.root.y = 0f

        binding.likeCaption.alpha = 0f
        binding.nopeCaption.alpha = 0f

        Glide.with(binding.avatar)
            .load(itemData.owner.avatarUrl)
            .placeholder(R.drawable.avatar_placeholder)
            .into(binding.avatar)

        binding.watchersCount.text = itemData.watchersCount.toString()
    }

    fun updateCaptionsTransparency(like: Boolean, a: Float) {
        val caption = if (like) {
            binding.likeCaption
        } else {
            binding.nopeCaption
        }
        caption.alpha = min(2 * a, 1f)
    }
}