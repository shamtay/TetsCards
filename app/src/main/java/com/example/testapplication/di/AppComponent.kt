package com.example.testapplication.di

import android.content.Context
import com.example.testapplication.MainActivity
import com.example.testapplication.di.modules.ApiServicesModule
import com.example.testapplication.di.modules.NetworkModule
import com.example.testapplication.di.modules.ViewModelsModule
import com.example.testapplication.di.scopes.ApplicationScope
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [ViewModelsModule::class, NetworkModule::class, ApiServicesModule::class]
)
interface AppComponent {


    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(mainActivity: MainActivity)
}