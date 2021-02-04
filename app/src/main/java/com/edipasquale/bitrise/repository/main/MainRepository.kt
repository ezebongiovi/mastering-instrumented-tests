package com.edipasquale.bitrise.repository.main

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.edipasquale.bitrise.db.entity.RecentAnimeEntity

interface MainRepository {

    fun fetchLatestAdditions(forceRefresh: Boolean): LiveData<PagedList<RecentAnimeEntity>>
}