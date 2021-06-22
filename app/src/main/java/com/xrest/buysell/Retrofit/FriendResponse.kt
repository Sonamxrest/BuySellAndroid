package com.xrest.buysell.Retrofit

data class FriendResponse(
    var success:Boolean?=null,
    var data:MutableList<Person>?=null
)
data class FriendResponses(
    var success:Boolean?=null,
    var data:MutableList<User>?=null
)
