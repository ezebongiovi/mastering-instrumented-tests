package com.edipasquale.bitrise.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.edipasquale.bitrise.databinding.HolderRecentBinding
import com.edipasquale.bitrise.db.entity.RecentAnimeEntity
import com.edipasquale.bitrise.view.adapter.holder.RecentAnimeViewHolder

class RecentAnimeAdapter :
    PagedListAdapter<RecentAnimeEntity, RecentAnimeViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentAnimeViewHolder {
        return RecentAnimeViewHolder(
            HolderRecentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecentAnimeViewHolder, position: Int) {
        getItem(position)?.let {
            holder.onBind(it)
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<RecentAnimeEntity>() {
        override fun areItemsTheSame(
            oldItem: RecentAnimeEntity,
            newItem: RecentAnimeEntity
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: RecentAnimeEntity,
            newItem: RecentAnimeEntity
        ): Boolean {
            return oldItem == newItem
        }
    }
}