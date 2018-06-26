package com.example.jobdownloader.listModule

import android.Manifest
import android.app.DownloadManager
import android.app.NotificationManager
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.NotificationCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.example.jobdownloader.R
import com.example.jobdownloader.adapters.BaseRecyclerAdapter
import com.example.jobdownloader.core.di.DIHandler
import com.example.jobdownloader.core.networking.DataResult
import com.example.jobdownloader.listModule.models.FileListResponse
import com.example.jobdownloader.utils.showToast
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : AppCompatActivity(), BaseRecyclerAdapter.CustomClickListener {

    @Inject
    lateinit var baseAdapter: BaseRecyclerAdapter

    lateinit var items: List<FileListResponse>
    private val PERMISSION_ALL = 0
    private var permissionGranted = false
    private val viewModel: MainViewModel by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DIHandler.getContainerComponent().inject(this)
        checkPermissions()
        registerReceiver(onComplete,
                IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        items = listOf()
        setUpRecyclerView()
        viewModel.getResults()
        observerData()
    }

    /*
    * Initialize recyclerView with empty list
    * */
    private fun setUpRecyclerView() {
        with(baseAdapter) {
            setLayoutId(R.layout.row_file_items)
            setItems(items)
            setCustomClickListener(this@MainActivity)
        }
        with(rvResultList) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = baseAdapter
        }
    }

    /*
    * Observe results from api call
    * */
    private fun observerData() {
        viewModel.postDataRepository.observe(this, Observer<DataResult<List<FileListResponse>>> { result ->
            when (result) {
                is DataResult.Progress -> progress.visibility = if (result.loading) View.VISIBLE else View.GONE
                is DataResult.Success -> {
                    items = result.data.subList(0, 20)
                    baseAdapter.run {
                        setItems(items)
                        notifyDataSetChanged()
                    }
                }
                is DataResult.Failure -> {
                    result.e.printStackTrace()
                    showToast("Check your internet connection!")
                }
            }
        })
    }

    /*
    * Handle download click event
    * */
    override fun onCustomClick(view: View, position: Int) {
        if (permissionGranted)
            viewModel.scheduleDownloadBatch(items[position])
        else
            checkPermissions()
    }

    /*
    * Broadcast receiver to handle downloadmanager results
    * */
    var onComplete: BroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(ctxt: Context, intent: Intent) {
            val referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            viewModel.list.remove(referenceId)
            if (viewModel.list.isEmpty()) {

                val mBuilder = NotificationCompat.Builder(this@MainActivity, "Download")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Job Downloader")
                        .setContentText("All Download completed")

                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(referenceId.toInt(), mBuilder.build())
            }

        }
    }

    /*
    * Check permissions
    * */
    private fun checkPermissions() {
        val PERMISSIONS = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (!hasPermissions(this, *PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL)
        }
    }

    fun hasPermissions(context: Context?, vararg permissions: String): Boolean {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_ALL) {
            if (permissions.size == 1 &&
                    permissions[0] == Manifest.permission.WRITE_EXTERNAL_STORAGE &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                permissionGranted = true
            } else {
                showToast("Please enable permission to download data")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(onComplete)
    }
}
