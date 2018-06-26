package com.example.jobdownloader.listModule

import android.app.DownloadManager
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.example.jobdownloader.core.di.DIHandler
import com.example.jobdownloader.core.networking.DataResult
import com.example.jobdownloader.listModule.dataManager.JobDownloaderRepository
import com.example.jobdownloader.listModule.models.FileListResponse
import com.example.jobdownloader.utils.toLiveData
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MainViewModel : ViewModel() {

    @Inject
    lateinit var repo: JobDownloaderRepository

    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    @Inject
    lateinit var downloadManager: DownloadManager


    val downloadBatchFiles: ArrayList<FileListResponse>
    val list: ArrayList<Long>

    init {
        DIHandler.getContainerComponent().inject(this)
        downloadBatchFiles = arrayListOf()
        list = arrayListOf()
    }

    /*
    * Converting publish object to live data to observe updates in the result
    * */
    val postDataRepository: LiveData<DataResult<List<FileListResponse>>> by lazy {
        repo.postFetchFileListDataResult.toLiveData(compositeDisposable)
    }

    /*
    * Function to make api call if cached results are not persisted
    * */
    fun getResults() {
        if (postDataRepository.value == null)
            repo.fetchFileList()
    }

    /*
    * Function to add download task, multiple files are downloaded parallely
    * */
    fun scheduleDownloadBatch(fileListResponse: FileListResponse) {
        if (!downloadBatchFiles.contains(fileListResponse)) {
            downloadBatchFiles.add(fileListResponse)
            list.addAll(repo.addToBatchDownload(list, downloadManager, fileListResponse))
            list.distinct()
        }
    }

    /*
    * Cleanup work
    * */
    override fun onCleared() {
        super.onCleared()
        DIHandler.destroyContainerComponent()
        compositeDisposable.clear()
    }

}