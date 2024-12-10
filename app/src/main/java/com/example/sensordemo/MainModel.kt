package com.example.sensordemo

import com.example.sensordemo.web.MainService
import com.example.sensordemo.web.bean.PostData
import com.p1ay1s.util.ServiceBuilder
import com.p1ay1s.util.ServiceBuilder.requestEnqueue

class MainModel {
    val mainService: MainService by lazy { ServiceBuilder.create<MainService>() }

    inline fun postJsonData(
        postData : PostData,
        crossinline onSuccess: (Int) -> Unit,
        crossinline onError: (Int?, String) -> Unit
    ) = requestEnqueue(mainService.postJsonData(postData), onSuccess, onError)
}