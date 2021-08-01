package com.xrest.buysell.Retrofit.Response

import com.xrest.buysell.Retrofit.Class.Request

class RequestResponse(
        val success:Boolean?=null,
        val data:MutableList<Request>?=null
) {
}
data class RequestSocketResponse(
        val success:Boolean?=null,
        val data:Request
){}