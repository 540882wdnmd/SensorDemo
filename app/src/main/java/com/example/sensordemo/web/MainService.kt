package com.example.sensordemo.web

import com.example.sensordemo.web.bean.PostData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * 后台规定的返回体
 *
 * 本来用 String 但是遇到报错:

 * "Expected a ${原定的类型} but was BEGIN_OBJECT at line 1 column 2 path $"
 *
 * 这个 Gson 错误意思是想帮你把返回体转化为你想要的类型但是失败了, 传过来的是个对象
 */
data class Response(
    val code: Int?,
    val msg: String?,
    val data: String?
)

interface MainService {
    @POST("api/save")
    fun postJsonData(
        @Body postData: PostData
    ): Call<Response>
}