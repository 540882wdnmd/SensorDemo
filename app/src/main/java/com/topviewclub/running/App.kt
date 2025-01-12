package com.topviewclub.running

import android.app.Application
import com.p1ay1s.base.appBaseUrl
import com.p1ay1s.base.appContext
import com.p1ay1s.base.log.ERROR
import com.p1ay1s.base.log.LOG_LEVEL
import com.p1ay1s.util.ServiceBuilder

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = this
        LOG_LEVEL = ERROR
        ServiceBuilder.enableLogger = true
        appBaseUrl = BASE
    }
}