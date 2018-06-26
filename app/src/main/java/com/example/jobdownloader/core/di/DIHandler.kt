package com.example.jobdownloader.core.di

import com.example.jobdownloader.base.BaseApplication
import com.example.jobdownloader.listModule.di.ContainerComponent
import com.example.jobdownloader.listModule.di.DaggerContainerComponent
import javax.inject.Singleton

@Singleton
object DIHandler {
    private var containerComponent: ContainerComponent? = null

    fun getContainerComponent(): ContainerComponent {
        if (containerComponent == null) {
            containerComponent = DaggerContainerComponent.builder().baseComponent(BaseApplication.baseComponent).build()
        }
        return containerComponent as ContainerComponent
    }

    fun destroyContainerComponent() {
        containerComponent = null
    }

}
