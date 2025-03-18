package com.package.msgapp

import android.app.Application
import com.package.msgapp.hilt.AppComponent
import com.package.msgapp.hilt.DaggerAppComponent
import com.package.msgapp.hilt.FirebaseModule

class MyApplication : Application() {
    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        // Dagger 2 oluşturma kodları.
        appComponent = DaggerAppComponent.builder()
            .firebaseModule(FirebaseModule())
            .build()
    }
}
