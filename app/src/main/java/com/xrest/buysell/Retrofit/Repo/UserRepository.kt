package com.xrest.buysell.Retrofit.Repo

import com.xrest.buysell.Retrofit.*
import com.xrest.buysell.Retrofit.Response.CommonResponse
import com.xrest.buysell.Retrofit.Response.FriendResponse
import com.xrest.buysell.Retrofit.Response.FriendResponses
import com.xrest.buysell.Retrofit.Response.LoginResponse
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
    suspend fun updateProfile(id:String,body:MultipartBody.Part): CommonResponse {
        return handleApiRequest {
            api.updateProfile(id,body)
        }
    }
    suspend fun updateDetails(user: User): CommonResponse
    {
        return handleApiRequest {
            api.update(user)
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





}