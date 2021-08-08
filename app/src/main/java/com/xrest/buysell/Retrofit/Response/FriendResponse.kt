package com.xrest.buysell.Retrofit.Response

import com.xrest.buysell.Retrofit.Person
import com.xrest.buysell.Retrofit.User

data class FriendResponse(
    var success:Boolean?=null,
    var data:MutableList<Person>?=null
)
data class FriendResponses(
    var success:Boolean?=null,
    var data:User?=null
)
