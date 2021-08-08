package com.xrest.buysell.Activity

import android.app.Dialog
import android.app.Service
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.button.MaterialButton
import com.xrest.buysell.R
import com.xrest.buysell.Retrofit.RetroftiService
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import org.jitsi.meet.sdk.JitsiMeet
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import org.json.JSONObject
import java.net.MalformedURLException
import java.net.URL


lateinit var mediaPlayer: MediaPlayer
lateinit var vibrator: Vibrator
class CallCheckActivity : AppCompatActivity() {
    lateinit var socket: Socket

    var url = "http://192.168.0.113:5000"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call_check)
        var button :MaterialButton = findViewById(R.id.call)
        socket = IO.socket(url)
        socket.connect()
        try {
            serverURL = URL("https://meet.jit.si")
            val defaultOptions = JitsiMeetConferenceOptions.Builder()
                .setServerURL(serverURL)
                .setWelcomePageEnabled(false)
                .build()
            JitsiMeet.setDefaultConferenceOptions(defaultOptions)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        button.setOnClickListener(){
            socket.emit("calling",RetroftiService.users!!._id)
        }
        socket.on("calling"){ data->
            CoroutineScope(Dispatchers.IO).launch {
               withContext(Main){
                   var dialog = Dialog(this@CallCheckActivity)
                   dialog.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT)
                   if(data[0].toString() !=RetroftiService.users!!._id){
dialog.setContentView(R.layout.recieving)
//                       vibratePlay()
                       var recieve:LottieAnimationView = dialog.findViewById(R.id.recieve)
                       recieve.setOnClickListener(){
                           socket.emit("recieved",data[0].toString())
                           val options = JitsiMeetConferenceOptions.Builder()
                               .setRoom("5545638")
                               .setWelcomePageEnabled(false)
                               .build()
                           JitsiMeetActivity.launch(this@CallCheckActivity, options)
                       }
                   }
                   else{
                       dialog.setContentView(R.layout.calling)
                   }
                   dialog.show()
               }
            }
        }

        socket.on("recieved"){data->
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Main){
                    if(data[0].toString() ==RetroftiService.users!!._id){
                        val options = JitsiMeetConferenceOptions.Builder()
                            .setRoom("5545638")
                            .setWelcomePageEnabled(false)
                            .build()
                        JitsiMeetActivity.launch(this@CallCheckActivity, options)
                    }
                }}
        }
    }

    @SuppressWarnings("MissingPermission")
    fun vibratePlay(){
        vibrator = this.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(200000, VibrationEffect.DEFAULT_AMPLITUDE))
            mediaPlayer = MediaPlayer.create(this,R.raw.rintone )
            mediaPlayer.start()
            Toast.makeText(this,"media playing", Toast.LENGTH_SHORT).show()
        } else {
            vibrator.vibrate(200000)
        }
    }

}
