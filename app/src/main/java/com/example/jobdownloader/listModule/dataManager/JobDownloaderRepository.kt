package com.example.jobdownloader.listModule.dataManager

import android.app.DownloadManager
import android.net.Uri
import com.example.jobdownloader.core.networking.DataResult
import com.example.jobdownloader.listModule.models.FileListResponse
import com.example.jobdownloader.utils.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject


class JobDownloaderRepository(private val remote: JobDownloaderBluePrint.Remote,
                              private val compositeDisposable: CompositeDisposable) : JobDownloaderBluePrint.Repository {

    /*
    * Initialize value of publish subject with a empty list
    * */
    override val postFetchFileListDataResult: PublishSubject<DataResult<List<FileListResponse>>> = PublishSubject.create<DataResult<List<FileListResponse>>>()

    override fun fetchFileList() {
        postFetchFileListDataResult.loading(true)
        remote.getFileList()
                .doOnBackOutOnMain()
                .subscribe({
                    postFetchFileListDataResult.success(it)
                }, { err -> handleError(err) })
                .addTo(compositeDisposable)
    }

    override fun addToBatchDownload(list: ArrayList<Long>, downloadManager: DownloadManager, fileListResponse: FileListResponse): ArrayList<Long> {
        val request = DownloadManager.Request(Uri.parse("${fileListResponse.postUrl}/download"))
                .apply {
                    setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                    setAllowedOverRoaming(false)
                    setTitle("${fileListResponse.filename} Downloading !!")
                    setDescription("Please wait while file is downloaded")
                    setVisibleInDownloadsUi(true)
                    setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    setDestinationInExternalPublicDir("/Picsum", fileListResponse.filename)
                }
        list.add(downloadManager.enqueue(request))
        return list
    }


    override fun handleError(error: Throwable) {
        postFetchFileListDataResult.failure(error)
    }
}