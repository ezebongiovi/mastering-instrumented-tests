package com.edipasquale.bitrise.view.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.edipasquale.bitrise.databinding.HolderRecentBinding
import com.edipasquale.bitrise.db.entity.RecentAnimeEntity

class RecentAnimeViewHolder(
    private val _binding: HolderRecentBinding
) : RecyclerView.ViewHolder(_binding.root) {

    fun onBind(item: RecentAnimeEntity) {
        Glide.with(_binding.root)
            .load(item.cover)
            .into(_binding.imageView)
    }
}
