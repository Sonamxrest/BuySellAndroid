package com.xrest.buysell.Retrofit.Repo

import com.xrest.buysell.Retrofit.*
import com.xrest.buysell.Retrofit.Response.CommonResponse
import com.xrest.buysell.Retrofit.Response.ProductResponse
import com.xrest.buysell.Retrofit.Routes.Products
import okhttp3.MultipartBody

 class ProductRepo: HandleApiReuest() {
    var api = RetroftiService.buildServices(Products::class.java)
    suspend fun post(post: Product): CommonResponse {

        return  handleApiRequest {
            api.post(RetroftiService.token!!,post)
        }

    }
    suspend fun uploadImage(id:String,body:MutableList<MultipartBody.Part>): CommonResponse {

        return handleApiRequest {
            api.upload(id,body)
        }

    }

    suspend fun  getPost(): ProductResponse {
        return handleApiRequest {
            api.getProduct()
        }
    }
 suspend fun  search(term:String): ProductResponse {
        return handleApiRequest {
            api.search(term)
        }
    }


    suspend fun Like(id:String): CommonResponse {
        return handleApiRequest {
            api.likeProduct(RetroftiService.token!!,id)
        }
    }
    suspend fun comment(comment: Comment): CommonResponse {
        return handleApiRequest {
            api.Comment(RetroftiService.token!!,comment)
        }
    }

    suspend fun update(id:String,post: Product): CommonResponse
    {
        return handleApiRequest {
            api.updateProduct(id,post)
        }
    }
    suspend fun delete(id:String): CommonResponse {
        return  handleApiRequest {
            api.deleteProduct(id)
        }
    }
    suspend fun sold(id:String): CommonResponse {
        return  handleApiRequest {
            api.soldProduct(id)
        }
    }

    suspend fun personsPost(id:String): ProductResponse {
        return handleApiRequest {
            api.seePersonPost(id)
        }
    }

     suspend fun updateComment(pid:String,oid:String,comment:String): CommonResponse {
         return handleApiRequest {
             api.updateComment(RetroftiService.token!!,pid,oid,comment)
         }
     }

     suspend fun deleteComment(pid:String,oid:String): CommonResponse
     {
         return handleApiRequest {
             api.deleteComment(RetroftiService.token!!,pid,oid)
         }
     }


}