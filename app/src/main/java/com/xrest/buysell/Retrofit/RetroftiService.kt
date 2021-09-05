package com.xrest.buysell.Retrofit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.jar.Manifest

object RetroftiService {
    val BASE_URL ="http://10.0.2.2:5000/"
    var token:String?=null
    var users: User?=null
    var isOnline: Boolean? =null
    val okHttpClient = OkHttpClient.Builder().connectTimeout(5,TimeUnit.MINUTES).writeTimeout(5,TimeUnit.MINUTES).readTimeout(5,TimeUnit.MINUTES)
    val retrofitBuilder = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(
        okHttpClient.build())
    val retrofit = retrofitBuilder.build()
    fun<T> buildServices(services:Class<T>):T{
        return retrofit.create(services)
    }

    fun getDataFromGallery(context: Context,data: Intent): String? {
var currentImage = data.data
        var imagePath = arrayOf(MediaStore.Images.Media.DATA)
            var contentResolver = context.contentResolver
                var cursor = contentResolver.query(currentImage!!,imagePath,null,null,null)
                    cursor!!.moveToFirst()
        var colIndex = cursor.getColumnIndex(imagePath[0])
        var data =cursor.getString(colIndex)
        cursor.close()
        return data

    }


     fun getDataFromCamera(context:Context,data:Intent):String{
         var bitmap = data.extras!!.get("data") as Bitmap
         var timeStamp = SimpleDateFormat("yyyyMMdd").format(Date())
         var file: File = bitmapToImage(context,bitmap,"${timeStamp}.jpg")
         return file.absolutePath
     }

    private fun bitmapToImage(context: Context,bitmap: Bitmap, s: String): File {
var file:File?=null

        try {
            file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + File.separator + s)
            file.createNewFile()
            var bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG,0,bos)
            var data = bos.toByteArray()
            var fos = FileOutputStream(file)
            fos.write(data)
            fos.flush()
            fos.close()


        }
        catch (ex:java.lang.Exception)
        {
            ex.printStackTrace()
        }

        return file!!
    }


    var permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.USE_FINGERPRINT,android.Manifest.permission.CAMERA,android.Manifest.permission.ACCESS_NETWORK_STATE)
    fun checkPermission(context:Activity){
        for (permission in permissions)
        {
            if(ActivityCompat.checkSelfPermission(context,permission)!=PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(context, permissions,1)

            }
        }

    }
    fun loadImage(image:String):String{
        return "${BASE_URL}uploads/${image}"
    }

}