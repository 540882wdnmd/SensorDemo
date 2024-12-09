package com.example.sensordemo

import com.example.sensordemo.bean.PostData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface MainService {
    @POST("/api/data")
    fun postJsonData(
        @Body postData: PostData
    ): Call<Void>
}