package com.topviewclub.running

import android.app.Application
import com.zephyr.base.appBaseUrl
import com.zephyr.base.appContext
import com.zephyr.base.log.ERROR
import com.zephyr.base.log.LOG_LEVEL

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = this
        LOG_LEVEL = ERROR
        appBaseUrl = BASE
    }
}