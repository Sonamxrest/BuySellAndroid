package com.xrest.buysell.Retrofit.Routes

import com.xrest.buysell.Retrofit.Comment
import com.xrest.buysell.Retrofit.Response.CommonResponse
import com.xrest.buysell.Retrofit.Product
import com.xrest.buysell.Retrofit.Response.LoginResponse
import com.xrest.buysell.Retrofit.Response.ProductResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface Products {

@POST("/post")
suspend fun post(@Header("Authorization")token:String , @Body post: Product):Response<CommonResponse>

@Multipart
@PUT("/post/upload/{id}")
suspend fun upload(@Path("id")id:String, @Part body:MutableList<MultipartBody.Part>):Response<CommonResponse>


    @Multipart
    @PUT("/uploadProduct/{id}/{item}")
    suspend fun uploadProduct(@Path("id")id:String, @Path("item")item:String, @Part bodt:MutableList<MultipartBody.Part>):Response<CommonResponse>

@GET("/get/product")
suspend fun getProduct():Response<ProductResponse>

@PUT("/update/product/{id}")
suspend fun updateProduct(@Path("id")id:String,@Body product: Product):Response<CommonResponse>

@PUT("/like/{id}")
suspend fun likeProduct(@Header("Authorization")token:String, @Path("id")id:String):Response<LoginResponse>


@PUT("/comment")
suspend fun Comment(@Header("Authorization")token: String,@Body comment: Comment):Response<CommonResponse>

@GET("person/post/{id}")
suspend fun seePersonPost(@Path("id")id:String):Response<ProductResponse>

@DELETE("/delete/{id}")
suspend fun deleteProduct(@Path("id")id:String):Response<CommonResponse>

@PUT("/sold/{id}")
suspend fun soldProduct(@Path("id")id:String):Response<CommonResponse>


@GET("/search/{name}")
suspend fun search(@Path("name")name:String):Response<ProductResponse>

    @FormUrlEncoded
    @PUT("/updateComment/{pid}/{oid}")
    suspend fun updateComment(
        @Header("Authorization") token: String,
        @Path("pid") id: String,
        @Path("oid") oid:String,
        @Field("comment") comment:String
    ): Response<CommonResponse>

    @PUT("/deleteComment/{pid}/{oid}")
    suspend fun deleteComment(
        @Header("Authorization") token: String,
        @Path("pid") id: String,
        @Path("oid") oid:String
    ): Response<CommonResponse>


}