package com.example.testapplication

import android.app.Application
import com.example.testapplication.di.AppComponent
import com.example.testapplication.di.DaggerAppComponent

class TestApplication: Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent
            .factory()
            .create(this)
    }
}