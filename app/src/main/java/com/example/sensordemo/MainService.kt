package com.example.sensordemo

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface MainService {
    @POST("asd")
    @FormUrlEncoded
    fun postJsonData(
        @Field("json") json: String
    ): Call<String> // String 暂定, 后面可以换成别的 dataclass
}