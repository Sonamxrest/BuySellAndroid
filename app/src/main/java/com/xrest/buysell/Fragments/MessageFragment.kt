package com.xrest.buysell.Fragments

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xrest.buysell.Adapters.ChatItem
import com.xrest.buysell.R
import com.xrest.buysell.Retrofit.InnerMessage
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.client.SocketIOException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import org.json.JSONObject
import java.util.*


class MessageFragment : Fragment(), View.OnClickListener,TextToSpeech.OnInitListener {
    lateinit var message: EditText
    lateinit var send: Button

    //    lateinit var image: ImageButton
//    lateinit var voice: ImageButton
    lateinit var textToSpeech: TextToSpeech
    var adapter = GroupAdapter<GroupieViewHolder>()
    lateinit var socket: Socket
    lateinit var rv: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_message, container, false)
        try {
            socket = IO.socket("http://192.168.0.105:5000")
            socket.connect()
        } catch (ex: SocketIOException) {
            ex.printStackTrace()
        }
//        var request = Request.Builder().url(url).build()
//        var okhttp = OkHttpClient()
//        ws = okhttp.newWebSocket(request, SocketListener())

        message = view.findViewById(R.id.message)
//        voice = view.findViewById(R.id.voice)
//        image = view.findViewById(R.id.image)
        send = view.findViewById(R.id.send)
        rv = view.findViewById(R.id.rv)
        rv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv.adapter = adapter
        textToSpeech = TextToSpeech(requireContext(), this)
        send.setOnClickListener() {
            var jsonObject = JSONObject()
            jsonObject.put("_id", "5555555")
            jsonObject.put("message", message.text.toString())
            socket.emit("message", jsonObject)
            message.setText(null)
        }
        message.setOnLongClickListener() {
            textToSpeech.speak(message.text.toString(), TextToSpeech.QUEUE_FLUSH, null)
            true
        }
        socket.on("message") {
            var message = JSONObject(it[0].toString())
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Main)
                {
                    adapter.add(
                        ChatItem(
                            requireContext(),
                            InnerMessage(message = message.getString("message"))
                        )
                    )
                    adapter.notifyDataSetChanged()
                    rv.smoothScrollToPosition(adapter.itemCount - 1)
                    Toast.makeText(
                        requireContext(),
                        "${message.getString("message")}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

//        voice.setOnClickListener(this)
        return view
    }

    override fun onResume() {
        super.onResume()
        var actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar!!.title = "Message"
        actionBar.setCustomView(R.layout.toolbar)

    }

    override fun onDestroy() {
        super.onDestroy()
        socket.disconnect()
    }

    override fun onPause() {
        super.onPause()
        socket.disconnect()
    }

    override fun onStop() {
        super.onStop()
        socket.disconnect()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == 1 && data != null) {

                var data = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                if (data != null) {
                    message.setText(data.get(0).toString())
                }


            }
        }


    }


    override fun onClick(v: View?) {
        when (v?.id) {
//            R.id.voice -> {
//
//                var intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
//                intent.putExtra(
//                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
//                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
//                )
//                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
//                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Please Speak Now")
//                try {
//
//                    startActivityForResult(intent, 1)
//                } catch (ex: ActivityNotFoundException) {
//                    ex.printStackTrace()
//                    Toast.makeText(
//                        requireContext(),
//                        "Your Device Does not support speechh to text",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//
//            }
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech.setLanguage(Locale.getDefault())

        }
    }

}
