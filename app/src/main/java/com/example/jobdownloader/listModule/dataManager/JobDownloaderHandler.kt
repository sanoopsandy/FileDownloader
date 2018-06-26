package com.example.jobdownloader.listModule.dataManager

import com.example.jobdownloader.core.networking.PostService
import com.example.jobdownloader.listModule.models.FileListResponse
import io.reactivex.Flowable

/*
* Class responsible for making api calls
* */
class JobDownloaderHandler(private val postService: PostService) : JobDownloaderBluePrint.Remote {
    override fun getFileList(): Flowable<List<FileListResponse>> {
        return postService.getFileList()
    }

}