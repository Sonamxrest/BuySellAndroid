package com.xrest.buysell.Retrofit.Response

import com.xrest.buysell.Retrofit.User

data class LoginResponse(
    val success:Boolean?=null,
    var token:String?=null,
    var user: User?=null
)