package com.xrest.buysell.Retrofit.Response

import com.xrest.buysell.Retrofit.Message


class MessageResponse(
        val success:Boolean?=null,
        val data:MutableList<Message>?=null
) {
}