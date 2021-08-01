package com.xrest.buysell.Retrofit.Class

import com.xrest.buysell.Retrofit.Person

data class Request(
        val _id:String?=null,
        val From:Person?=null,
        val To: Person?=null


) {
}