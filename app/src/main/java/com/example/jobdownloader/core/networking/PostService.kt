package com.example.jobdownloader.core.networking

import com.example.jobdownloader.listModule.models.FileListResponse
import io.reactivex.Flowable
import retrofit2.http.GET

interface PostService {
    @GET("/list")
    fun getFileList(): Flowable<List<FileListResponse>>
}