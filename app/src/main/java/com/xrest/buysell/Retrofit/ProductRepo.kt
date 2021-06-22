package com.xrest.buysell.Retrofit

import okhttp3.MultipartBody

class ProductRepo:HandleApiReuest() {
    var api = RetroftiService.buildServices(Products::class.java)
    suspend fun post(post:Product):CommonResponse{

        return  handleApiRequest {
            api.post(RetroftiService.token!!,post)
        }

    }
    suspend fun uploadImage(id:String,body:MutableList<MultipartBody.Part>):CommonResponse{

        return handleApiRequest {
            api.upload(id,body)
        }

    }
}