package com.example.sensordemo

import android.app.Application
import com.p1ay1s.base.appBaseUrl
import com.p1ay1s.base.appContext
import com.p1ay1s.base.log.ERROR
import com.p1ay1s.base.log.LOG_LEVEL
import com.p1ay1s.util.ServiceBuilder

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        LOG_LEVEL = ERROR
        ServiceBuilder.enableLogger = true
        appBaseUrl = "http://192.168.10.103:3000/"
        appContext = this
    }
}