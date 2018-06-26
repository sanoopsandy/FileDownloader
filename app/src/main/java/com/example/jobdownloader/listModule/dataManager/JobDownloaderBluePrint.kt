package com.example.jobdownloader.listModule.dataManager

import android.app.DownloadManager
import com.example.jobdownloader.core.networking.DataResult
import com.example.jobdownloader.listModule.models.FileListResponse
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject

/*
* Blueprint of all the functions that will be used to fetch, store and access data objects
* */
interface JobDownloaderBluePrint {
    interface Repository {
        val postFetchFileListDataResult: PublishSubject<DataResult<List<FileListResponse>>>
        fun addToBatchDownload(list: ArrayList<Long>, downloadManager: DownloadManager, fileListResponse: FileListResponse): ArrayList<Long>
        fun fetchFileList()
        fun handleError(error: Throwable)
    }

    interface Remote {
        fun getFileList(): Flowable<List<FileListResponse>>
    }
}