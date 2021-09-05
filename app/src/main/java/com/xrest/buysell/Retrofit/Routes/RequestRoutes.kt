package com.xrest.buysell.Retrofit.Routes

import com.xrest.buysell.Retrofit.Class.Request
import com.xrest.buysell.Retrofit.Response.CommonResponse
import com.xrest.buysell.Retrofit.Response.LoginResponse
import com.xrest.buysell.Retrofit.Response.RequestResponse
import com.xrest.buysell.Retrofit.Response.RequestSocketResponse
import retrofit2.Response
import retrofit2.http.*

interface RequestRoutes {


@POST("/sendRequest/{id}")
suspend fun sendRequest(@Header("Authorization")token:String,@Path("id") id:String): Response<CommonResponse>
@PUT("/acceptRequest")
suspend fun acceptRequest(@Header("Authorization")token:String,@Body message: Request):Response<LoginResponse>
@GET("/showRequest")
suspend fun getRequest(@Header("Authorization")token:String):Response<RequestResponse>
@DELETE("/deleteRequest/{id}")
suspend fun  deleteReq( @Path("id")data:String):Response<CommonResponse>

}