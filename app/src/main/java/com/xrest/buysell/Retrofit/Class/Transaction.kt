package com.xrest.buysell.Retrofit.Class

import com.xrest.buysell.Retrofit.Person
import com.xrest.buysell.Retrofit.Product

data class Transaction(
    val Sender : Person? =null,
    var Reciever:Person?=null,
    var Date:String?=null,
    var Amount:Int?=null,
    var Description:Int?=null,
    var Product:Product?=null
) {
}