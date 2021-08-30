package com.xrest.buysell.Retrofit.Response

import com.xrest.buysell.Retrofit.Class.Transaction

class tresponse(
    val success:Boolean?=null,
    var data: MutableList<Transaction>?=null
) {
}