package com.xrest.buysell.Retrofit.Routes

import com.xrest.buysell.Retrofit.*
import com.xrest.buysell.Retrofit.Response.CommonResponse
import com.xrest.buysell.Retrofit.Response.FriendResponse
import com.xrest.buysell.Retrofit.Response.FriendResponses
import com.xrest.buysell.Retrofit.Response.LoginResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface UserRoutes {


    @POST("/insert/user")
    suspend fun register(@Body user: User):Response<CommonResponse>
    @FormUrlEncoded
    @POST("/login")
    suspend fun login(@Field("Username") username:String,@Field("Password") passwor:String):Response<LoginResponse>
    @Multipart
    @PUT("/update/profile/{id}")
    suspend fun updateProfile(@Path("id") id:String, @Part body:MultipartBody.Part):Response<CommonResponse>
     @PUT("/update/user")
    suspend fun update(@Header("Authorization") token: String,@Body user: User):Response<CommonResponse>
    @GET("/all")
    suspend fun getAllUsers():Response<FriendResponse>
    @GET("/showFriends")
    suspend fun showFriends(@Header("authorization")token:String):Response<FriendResponses>
    @PUT("/logout")
    suspend fun logout(@Header("authorization") token:String):Response<CommonResponse>
    @GET("/user/{number}")
    suspend fun getUser(@Path("number")number:String):Response<LoginResponse>
    @FormUrlEncoded
    @PUT("/changePassword/{id}")
    suspend fun changePassword(@Field("np")password:String,@Path("id")id:String):Response<CommonResponse>
    @GET("/wishList")
    suspend fun getWish(@Header("Authorization") token:String):Response<LoginResponse>

}