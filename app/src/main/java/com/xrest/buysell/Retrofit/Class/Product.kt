package com.xrest.buysell.Retrofit

import java.io.Serializable

data class Product(

    val _id:String?=null,
    val User:Person?=null,
    val Name:String?=null,
    var createdAt:String?=null,
    var Category:String?=null,
    var SubCategory:String?=null,
    var Price:String?=null,
    var Negotiable:Boolean?=null,
    var SoldOut:Boolean?=null,
    var UsedFor:Int?=null,
    var Condition:String?=null,
    var Description:String?=null,
    var Images:MutableList<String>?=null,
    var Likes:MutableList<String>?=null,
    var Features:MutableList<Features>?=null,
    var Comments:MutableList<Comment>?=null
):Serializable

data class Features(
    var _id:String?=null,
    var name:String?=null,
    var feature:String?=null
):Serializable
data class Comment(
    var _id:String?=null,
    var user:Person?=null,
    var comment:String?=null
):Serializable

