package com.example.sensordemo.web

import com.example.sensordemo.web.bean.PostData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface MainService {
    @POST("api/save")
    fun postJsonData(
        @Body postData: PostData
    ): Call<Int>
}