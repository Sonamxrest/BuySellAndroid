package com.xrest.buysell.Retrofit.Response

import com.xrest.buysell.Retrofit.Product

data class ProductResponse(
    val success:Boolean?=null,
    val data:MutableList<Product>?=null
)