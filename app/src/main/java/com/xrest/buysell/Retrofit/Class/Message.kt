package com.xrest.buysell.Retrofit

import java.io.Serializable

data class Message(
        val _id:String?=null,
    val Sender:Users?=null,
    val Reciever:Users?=null,
val Messages:MutableList<InnerMessage>?=null


): Serializable
data class InnerMessage(
        val val_id:String?=null,
        val message:String?=null,
    val user:Person?=null,
        val format:String?=null
):Serializable