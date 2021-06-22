package com.xrest.buysell.Retrofit

import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException
import java.lang.StringBuilder

abstract class HandleApiReuest {
    suspend fun<T> handleApiRequest(call:suspend () -> Response<T>):T{

        val response = call.invoke()
        if(response.isSuccessful)
        {
            return  response.body()!!
        }
        else{
            var string = StringBuilder()
            response.errorBody().toString().let {
                try {
                    string.append(JSONObject(it).getString("success"))
                }
                catch (ex:JSONException)
                {

                }
                string.append("ErrorCode: ${response.code()}")
            }
            println(string)
            throw IOException(string.toString())

        }
    }


}