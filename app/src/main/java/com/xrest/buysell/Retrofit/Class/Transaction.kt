package com.xrest.buysell.Retrofit.Class

import com.xrest.buysell.Retrofit.Person

data class Transaction(
    val Sender : Person? =null,
    var Reciever:Person?=null,
    var Date:String?=null,
    var Amount:Int?=null,
    var Description:Int?=null
) {
}