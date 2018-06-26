package com.example.jobdownloader.listModule.models

import com.google.gson.annotations.SerializedName

data class FileListResponse(@SerializedName("id") val id: Int,
                            @SerializedName("filename") val filename: String,
                            @SerializedName("author") val author: String,
                            @SerializedName("author_url") val authorUrl: String,
                            @SerializedName("post_url") val postUrl: String
)