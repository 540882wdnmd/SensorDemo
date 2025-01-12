package com.topviewclub.running

import com.topviewclub.running.web.MainService
import com.topviewclub.running.web.Response
import com.topviewclub.running.web.bean.PostData
import com.zephyr.util.ServiceBuilder
import com.zephyr.util.ServiceBuilder.requestEnqueue

class MainModel {
    val mainService: MainService by lazy { ServiceBuilder.create<MainService>() }

    inline fun postJsonData(
        postData: PostData,
        crossinline onSuccess: (Response?) -> Unit,
        crossinline onError: (Int?, String) -> Unit
    ) = requestEnqueue(mainService.postJsonData(postData), onSuccess, onError)
}