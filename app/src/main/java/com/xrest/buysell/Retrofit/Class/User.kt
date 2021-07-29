package com.xrest.buysell.Retrofit

import java.io.Serializable

data class User(
    var _id:String?=null,
    var Name:String?=null,
    var Username:String?=null,
    var PhoneNumber:String?=null,
    var Profile:String?=null,
    var Password:String?=null,
    var isOnline:Boolean?=null,
    var Friends:MutableList<Users>?=null,
    var Questions:MutableList<Question>?=null,
    var Likes:MutableList<Productss>?=null
):Serializable

data class Productss(
    val _id:String?=null,
    val product:String?=null
):Serializable
data class Question(
    val question:String?=null,
    val answer:String?=null
):Serializable

data class Users(
    val _id:String?=null,
    val user:User?=null
):Serializable

data class Person(
    var _id:String?=null,
    var Name:String?=null,
    var Username:String?=null,
    var PhoneNumber:String?=null,
    var Profile:String?=null,
    var isOnline:Boolean?=null
):Serializable