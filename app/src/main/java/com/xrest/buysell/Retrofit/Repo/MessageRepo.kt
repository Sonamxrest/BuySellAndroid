package com.xrest.buysell.Retrofit.Repo

import com.xrest.buysell.Retrofit.*
import com.xrest.buysell.Retrofit.Response.CommonResponse
import com.xrest.buysell.Retrofit.Response.MessageResponse
import com.xrest.buysell.Retrofit.Routes.MessageRoutes
import okhttp3.MultipartBody

class MessageRepo: HandleApiReuest(){
val api = RetroftiService.buildServices(MessageRoutes::class.java)



    suspend fun getMessages(): MessageResponse {
        return  handleApiRequest {
            api.getMessage(RetroftiService.token!!)
        }
    }
    suspend fun getMes(id:String): SingleMessageResponse {

        return handleApiRequest {
            api.getMes(id)
        }

    }

    suspend fun  createInbox(Userid:String): CommonResponse {
        return handleApiRequest {
            api.creatInbox(Userid, RetroftiService.token!!)
        }
    }
    suspend fun sendMessages(messageId:String,message: InnerMessage):CommonResponse{
        return handleApiRequest {
            api.sendMessage(messageId, RetroftiService.token!!,message)
        }
    }
suspend fun sendImage(id:String,body: MultipartBody.Part) :CommonResponse{
    return handleApiRequest {
        api.sendImage(id=id,token= RetroftiService.token!!,body=body)
    }
}


}