package com.example.jobdownloader.base

import android.app.Application
import com.example.jobdownloader.core.di.AppModule
import com.example.jobdownloader.core.di.BaseComponent
import com.example.jobdownloader.core.di.NetModule
import com.example.jobdownloader.core.Constants
import com.example.jobdownloader.core.di.DaggerBaseComponent

class BaseApplication : Application() {

    companion object {
        lateinit var baseComponent: BaseComponent
    }

    override fun onCreate() {
        super.onCreate()
        initDI()
    }

    private fun initDI() {
        baseComponent = DaggerBaseComponent.builder()
                .appModule(AppModule(this))
                .netModule(NetModule(Constants.BASE_URL))
                .build()
    }

}