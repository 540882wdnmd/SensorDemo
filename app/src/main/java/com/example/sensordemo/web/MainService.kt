package com.example.sensordemo.web

import com.example.sensordemo.web.bean.PostData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface MainService {
    @POST("post-json-data")
    fun postJsonData(
        @Body postData: PostData
    ): Call<Int>
}