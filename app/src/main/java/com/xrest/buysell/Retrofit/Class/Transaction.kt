package com.xrest.buysell.Retrofit.Class

import com.xrest.buysell.Retrofit.Person
import com.xrest.buysell.Retrofit.Prod
import java.util.*

data class Transaction(
    val Sender : Person? =null,
    var Reciever:Person?=null,
    var Date: Date?=null,
    var Amount:Int?=null,
    var Description:String?=null,
    var Product:Prod?=null
) {
}