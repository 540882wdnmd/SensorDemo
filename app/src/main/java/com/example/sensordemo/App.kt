package com.example.sensordemo

import android.app.Application
import com.p1ay1s.base.appContext

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = this
    }
}