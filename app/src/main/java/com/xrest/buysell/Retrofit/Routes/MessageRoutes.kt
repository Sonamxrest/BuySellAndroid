package com.xrest.buysell.Retrofit.Routes

import com.xrest.buysell.Retrofit.InnerMessage
import com.xrest.buysell.Retrofit.Response.MessageResponse
import com.xrest.buysell.Retrofit.Response.CommonResponse
import com.xrest.buysell.Retrofit.SingleMessageResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface MessageRoutes {
    @GET("/getAllMessages")
    suspend fun getMessage(@Header("Authorization") token: String): Response<MessageResponse>

    @GET("/messages/{id}")
    suspend fun getMes(@Path("id") id: String): Response<SingleMessageResponse>

    @POST("/startChatting/{id}")
    suspend fun creatInbox(@Path("id") userId: String?, @Header("Authorization") token: String?): Response<CommonResponse>

    @PUT("/message/{id}")
    suspend fun sendMessage(@Path("id") messageId: String, @Header("Authorization") token: String, @Body data: InnerMessage): Response<CommonResponse>

    @Multipart
    @PUT("/message/{id}")
    suspend fun sendImage(@Path("id") id: String, @Header("Authorization") token: String, @Part body: MultipartBody.Part): Response<CommonResponse>
}