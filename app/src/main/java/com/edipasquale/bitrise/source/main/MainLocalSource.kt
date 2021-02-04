package com.edipasquale.bitrise.source.main

import androidx.paging.toLiveData
import com.edipasquale.bitrise.db.AnimeDao
import com.edipasquale.bitrise.db.entity.RecentAnimeEntity

class MainLocalSource(
    private val dao: AnimeDao
) {

    fun fetchLatestAdditions() = dao.getAllRecents().toLiveData(
        pageSize = 10
    )

    fun insertRecents(list: List<RecentAnimeEntity>) {
        dao.insertRecents(list)
    }
}