package com.xrest.buysell.Retrofit.Repo

import com.xrest.buysell.Retrofit.Class.Request
import com.xrest.buysell.Retrofit.HandleApiReuest
import com.xrest.buysell.Retrofit.Response.CommonResponse
import com.xrest.buysell.Retrofit.Response.RequestResponse
import com.xrest.buysell.Retrofit.Response.RequestSocketResponse
import com.xrest.buysell.Retrofit.RetroftiService
import com.xrest.buysell.Retrofit.Routes.RequestRoutes


class RequestRepo:HandleApiReuest() {
    val api = RetroftiService.buildServices(RequestRoutes::class.java)

    suspend fun  sendRequest(id:String):RequestSocketResponse{
        return handleApiRequest {
            api.sendRequest(RetroftiService.token!!,id)
        }

    }
    suspend fun acceptReq(data: Request):CommonResponse{
        return handleApiRequest {
            api.acceptRequest(RetroftiService.token!!,data)
        }
    }

    suspend fun getRequest(): RequestResponse {
        return handleApiRequest {
            api.getRequest(RetroftiService.token!!)
        }
    }
    suspend fun deleteRequest(data:String): CommonResponse {
        return handleApiRequest {
            api.deleteReq(data)
        }
    }


}