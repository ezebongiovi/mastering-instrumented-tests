package com.edipasquale.bitrise.source.main

import android.app.Application
import androidx.paging.toLiveData
import androidx.room.Room
import com.edipasquale.bitrise.db.AppDatabase
import com.edipasquale.bitrise.db.entity.RecentAnimeEntity

class MainLocalSource(
    application: Application
) {
    private val database = Room.databaseBuilder(application, AppDatabase::class.java, "main")
        .build()

    fun fetchLatestAdditions() = database.dao().getAllRecents().toLiveData(
        pageSize = 10
    )

    fun insert(list: List<RecentAnimeEntity>) {
        database.dao().insertRecents(list)
    }
}