package com.edipasquale.bitrise.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class RecentAnimeEntity(
    @PrimaryKey
    @SerializedName("vid_id")
    val id: String,
    val cover: String,
    val title: String,
    @SerializedName("date")
    val formattedDate: String
)