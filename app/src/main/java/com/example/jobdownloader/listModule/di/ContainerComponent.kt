package com.example.jobdownloader.listModule.di

import android.app.DownloadManager
import android.content.Context
import com.example.jobdownloader.adapters.BaseRecyclerAdapter
import com.example.jobdownloader.core.di.BaseComponent
import com.example.jobdownloader.core.networking.PostService
import com.example.jobdownloader.listModule.MainActivity
import com.example.jobdownloader.listModule.MainViewModel
import com.example.jobdownloader.listModule.dataManager.JobDownloaderBluePrint
import com.example.jobdownloader.listModule.dataManager.JobDownloaderHandler
import com.example.jobdownloader.listModule.dataManager.JobDownloaderRepository
import dagger.Component
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Retrofit

@ContainerScope
@Component(dependencies = [BaseComponent::class], modules = [ContainerModule::class])
interface ContainerComponent {
    fun inject(mainViewModel: MainViewModel)
    fun inject(mainActivity: MainActivity)
}

@ContainerScope
@Module
class ContainerModule {
    @ContainerScope
    @Provides
    fun getAdapter() = BaseRecyclerAdapter()

    @ContainerScope
    @Provides
    fun getListRepo(remote: JobDownloaderBluePrint.Remote,
                    compositeDisposable: CompositeDisposable): JobDownloaderRepository = JobDownloaderRepository(remote, compositeDisposable)

    @ContainerScope
    @Provides
    fun getRemote(postService: PostService): JobDownloaderBluePrint.Remote = JobDownloaderHandler(postService)

    @ContainerScope
    @Provides
    fun getCompositeDisposable(): CompositeDisposable = CompositeDisposable()

    @ContainerScope
    @Provides
    fun getPostService(retrofit: Retrofit): PostService = retrofit.create(PostService::class.java)

    @ContainerScope
    @Provides
    fun getDownloadManager(context: Context) = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

}