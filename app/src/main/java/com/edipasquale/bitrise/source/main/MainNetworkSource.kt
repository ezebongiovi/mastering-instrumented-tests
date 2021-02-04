package com.edipasquale.bitrise.source.main

import com.edipasquale.bitrise.db.entity.RecentAnimeEntity
import com.edipasquale.bitrise.dto.api.APIResponse
import retrofit2.Call
import retrofit2.http.GET

interface MainNetworkSource {

    @GET("anime/list/recent")
    fun fetchLatestAdditions(): Call<APIResponse<List<RecentAnimeEntity>>>
}