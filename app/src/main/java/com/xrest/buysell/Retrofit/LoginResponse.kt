package com.xrest.buysell.Retrofit

data class LoginResponse(
    val success:Boolean?=null,
    var token:String?=null,
    var user:User?=null
)