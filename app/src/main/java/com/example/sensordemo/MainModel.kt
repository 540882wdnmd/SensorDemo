package com.example.sensordemo

import com.p1ay1s.util.ServiceBuilder
import com.p1ay1s.util.ServiceBuilder.requestEnqueue

class MainModel {
    val mainService: MainService by lazy { ServiceBuilder.create<MainService>() }

    inline fun postJsonData(
        json: String,
        crossinline onSuccess: (String) -> Unit, // 返回体暂定为 String
        crossinline onError: (Int?, String) -> Unit
    ) = requestEnqueue(mainService.postJsonData(json), onSuccess, onError)
}