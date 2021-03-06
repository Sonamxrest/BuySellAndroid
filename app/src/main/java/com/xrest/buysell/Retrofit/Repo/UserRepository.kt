package com.xrest.buysell.Retrofit.Repo

import com.xrest.buysell.Retrofit.*
import com.xrest.buysell.Retrofit.Response.*
import com.xrest.buysell.Retrofit.Routes.UserRoutes
import okhttp3.MultipartBody

class UserRepository: HandleApiReuest() {
    var api = RetroftiService.buildServices(UserRoutes::class.java)
    suspend fun login(username:String,password:String): LoginResponse {
        return handleApiRequest {
            api.login(username,password)
        }
    }
    suspend fun insert(user: User): CommonResponse
    {
        return handleApiRequest {
            api.register(user)
        }
    }
    suspend fun updateProfile(id:String,body:MultipartBody.Part): LoginResponse {
        return handleApiRequest {
            api.updateProfile(id,body)
        }
    }
    suspend fun updateDetails(user: User): CommonResponse
    {
        return handleApiRequest {
            api.update(RetroftiService.token!!,user)
        }
    }
    suspend fun updatePassword(op:String,id:String): CommonResponse {
        return handleApiRequest {
            api.changePassword(op,id)
        }
    }
    suspend fun showAll(): FriendResponse {
        return handleApiRequest {
            api.getAllUsers()
        }
    }
    suspend fun showFriends(): FriendResponses {
        return handleApiRequest {
            api.showFriends(RetroftiService.token!!)
        }
    }
    suspend fun logout(): CommonResponse
    {
        return handleApiRequest {
            api.logout(RetroftiService.token!!)
        }
    }

    suspend fun getUser(number:String): LoginResponse {
        return handleApiRequest {
            api.getUser(number)
        }
    }

suspend fun getWish():LoginResponse{
    return handleApiRequest {
        api.getWish(RetroftiService.token!!)
    }
}

    suspend fun getUsers(id:String):LoginResponse{
        return handleApiRequest {
            api.getUsers(id)
        }
    }
suspend fun rate(id:String, rating:String):LoginResponse{
    return handleApiRequest {
        api.rate(RetroftiService.token!!,id,rating)
    }
}
suspend fun pay(toId:String, amount:String, desc:String,id:String):LoginResponse{
    return handleApiRequest {
        api.pay(RetroftiService.token!!,toId,amount,desc,id)
    }
}
suspend fun tr():tresponse{
    return handleApiRequest {
        api.trans(RetroftiService.token!!)
    }
}
    suspend fun check(passowrd:String):CommonResponse{
        return handleApiRequest {
            api.check(RetroftiService.token!!,passowrd)
        }
    }
}