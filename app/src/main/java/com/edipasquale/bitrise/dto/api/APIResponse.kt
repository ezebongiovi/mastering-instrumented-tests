package com.edipasquale.bitrise.dto.api

import com.google.gson.annotations.SerializedName

data class APIResponse<T>(
    @SerializedName("api_info")
    val apiInfo: String? = null,
    @SerializedName("request_info")
    val requestInfo: String? = null,
    @SerializedName("data")
    val data: T? = null
)