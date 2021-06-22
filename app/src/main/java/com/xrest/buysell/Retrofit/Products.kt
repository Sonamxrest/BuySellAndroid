package com.xrest.buysell.Retrofit

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface Products {

@POST("/post")
suspend fun post(@Header("Authorization")token:String , @Body post:Product):Response<CommonResponse>

@Multipart
@PUT("/post/upload/{id}")
suspend fun upload(@Path("id")id:String, @Part() body:MutableList<MultipartBody.Part>):Response<CommonResponse>


}