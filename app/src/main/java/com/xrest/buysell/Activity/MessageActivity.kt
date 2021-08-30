package com.xrest.buysell.Activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PixelFormat
import android.media.MediaPlayer
import android.os.*
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.xrest.buysell.Adapters.ChatItem
import com.xrest.buysell.Adapters.ChatItem2
import com.xrest.buysell.Adapters.ImageAdapter
import com.xrest.buysell.Adapters.ImageAdapter2
import com.xrest.buysell.R
import com.xrest.buysell.Retrofit.InnerMessage
import com.xrest.buysell.Retrofit.Person
import com.xrest.buysell.Retrofit.Repo.MessageRepo
import com.xrest.buysell.Retrofit.Repo.UserRepository
import com.xrest.buysell.Retrofit.RetroftiService
import com.xrest.buysell.Retrofit.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import okhttp3.*
import org.jitsi.meet.sdk.JitsiMeet
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.net.MalformedURLException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


val adapter = GroupAdapter<GroupieViewHolder>()
lateinit var rv:RecyclerView
lateinit var id:String
lateinit var user:String
lateinit var serverURL: URL
lateinit var ib: ImageButton
lateinit var image: ImageView
lateinit var ed:EditText
lateinit var type:String
lateinit var dialog : Dialog
var cameraCode=1
var galleryCode=0
lateinit var socket:WebSocket
lateinit var toUser:User

class MessageActivity : AppCompatActivity(), View.OnClickListener {

    val url="ws://10.0.2.2:5000"
    var okHttpClient = OkHttpClient()
    val request= Request.Builder().url(url).build()
    var img:String?=null
    lateinit var mediaPlayer: MediaPlayer
    lateinit var vibrator: Vibrator
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        dialog = Dialog(this@MessageActivity)
        adapter.clear()
        socket = okHttpClient.newWebSocket(request, SocketListener(applicationContext))
        type="Message"
        id = intent.getStringExtra("id")!!
        user = intent.getStringExtra("user")!!
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
        CoroutineScope(Dispatchers.IO).launch {
    var response = UserRepository().getUser(user)
    if(response.success == true)
    {
        withContext(Main){
            toUser = response.user!!
            supportActionBar!!.setTitle(response.user!!.Name!!)
        }
    }
}


        ib = findViewById(R.id.imageb)
        image = findViewById(R.id.image)
        image.isVisible=false
        rv = findViewById(R.id.rv)
        ed = findViewById(R.id.message)
        var send: Button =findViewById(R.id.send)
        actionBar?.setTitle("Messages")
        rv.layoutManager= LinearLayoutManager(this)
        CoroutineScope(Dispatchers.IO).launch {
            val repo = MessageRepo()
            val response = repo.getMes(id)
            if(response.success==true)
            {
                withContext(Dispatchers.Main){
                    for(data in response.data?.Messages!!)
                    {
                        if(data.user?._id==RetroftiService.users?._id)
                        {
                            if(data.format=="Image")
                            {
                                adapter.add(ImageAdapter2(this@MessageActivity, data))
                            }
                            else{
                                adapter.add(ChatItem2(applicationContext, data))
                            }
                        }
                        else{
                            if(data.format=="Image")
                            {
                                adapter.add(ImageAdapter(this@MessageActivity, data))

                            }
                            else{
                                adapter.add(ChatItem(applicationContext, data))
                            }
                        }

                    }
                }
            }
        }
        send.setOnClickListener(this)
        ib.setOnClickListener(this)

        rv.adapter =adapter
//        socketIo.on("calling"){ data->
//            Log.d("call",data[0].toString())
//
//
//        }
//
//        socketIo.on("recieved"){data->
//            Log.d("call",data[0].toString())
//
//        }


    }

    override fun onRestart() {
        super.onRestart()
        socket = okHttpClient.newWebSocket(request, SocketListener(applicationContext))

    }
    override fun onStop() {
        socket.close(1000, "Bye")
        socket.cancel()
        super.onStop()
    }
    fun sendMessage(){
        Log.d("sxxx", type)
        val message = ed.text.toString()
        val repo = MessageRepo()



            var json = JSONObject()
            if(img==null)
            {
                CoroutineScope(Dispatchers.IO).launch {
                    var response =
                        repo.sendMessages(id, InnerMessage(message = message, format = type))
                    if (response.success == true) {

                        try {
                            json.put("id", id)
                            json.put("message", message)
                            json.put("user", RetroftiService.users?._id)
                            json.put("format", "Message")

                            socket.send(json.toString())
                            withContext(Dispatchers.Main)
                            {
                                image.isVisible = false
                                ed.setText(null)
                                rv.smoothScrollToPosition(adapter.getItemCount());
                            }

                        } catch (ex: JSONException) {
                            ex.printStackTrace()
                        }

                    }
                }
            }
            else if(img!=null)
            {
                CoroutineScope(Dispatchers.IO).launch {
                val file = File(img)
                val extention = MimeTypeMap.getFileExtensionFromUrl(img)
                val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extention)
                val reqFile = RequestBody.create(MediaType.parse(mimeType), file)
                val body = MultipartBody.Part.createFormData("image", file.name, reqFile)
               var responses = repo.sendImage(id, body)
                Log.d("response", responses!!.message.toString())
                Log.d("response", responses!!.success.toString())
                if(responses.success==true)
                {
                    try {

                        json.put("id", id)
                        json.put("message", responses.message)
                        json.put("user", RetroftiService.users?._id)
                        json.put("format", "Image")
                        delay(2000)
                        socket.send(json.toString())
                        withContext(Dispatchers.Main)
                        {
                            image.isVisible=false
                            ed.setText(null)
                            rv.smoothScrollToPosition(adapter.getItemCount());
                            img=null
                        }


                    } catch (ex: JSONException) {
                        ex.printStackTrace()
                    }

                }
            }



        }




    }

    fun openCamera(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, cameraCode)
        image.isVisible= true



    }
    fun openGallery(){

        val intent = Intent(Intent.ACTION_PICK)
        intent.type ="image/*"
        startActivityForResult(intent, galleryCode)
        image.isVisible=true

    }

    override fun onClick(v: View?) {
        when(v?.id)
        {
            R.id.imageb -> {
                val pop = PopupMenu(this, ib)
                pop.menuInflater.inflate(R.menu.camera_gallery, pop.menu)
                pop.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.gallery -> {
                            openGallery()
                        }
                        R.id.camera -> {
                            openCamera()
                        }
                    }
                    true
                }
                img = "asdasdasd"
                pop.show()
            }
            R.id.send -> {

                sendMessage()

                type = "Message"


            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == galleryCode && data != null) {
                val selectedImage = data.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val contentResolver = contentResolver
                val cursor =
                    contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
                cursor!!.moveToFirst()
                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                img = cursor.getString(columnIndex)
                image.setImageBitmap(BitmapFactory.decodeFile(img))
                type="Image"

                cursor.close()
            } else if (requestCode == cameraCode && data != null) {
                val imageBitmap = data.extras?.get("data") as Bitmap
                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val file = bitmapToFile(imageBitmap, "$timeStamp.jpg")
                img = file!!.absolutePath
                image.setImageBitmap(BitmapFactory.decodeFile(img))
                type="Image"
            }
        }
    }

    private fun bitmapToFile(bitmap: Bitmap, timeStamp: String): File? {
        var file: File?=null
        try{

            file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + File.separator + timeStamp)
            file.createNewFile()

            var byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream)
            val data = byteArrayOutputStream.toByteArray()
            val fileOutputStream = FileOutputStream(file)
            fileOutputStream.write(data)
            fileOutputStream.flush()
            fileOutputStream.close()
        }
        catch (ex: java.lang.Exception)
        {

        }
        return file
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.call, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.call -> {
                var json = JSONObject()
                json.put("from", RetroftiService.users!!._id)
                json.put("id", id)
                json.put("message", "${toUser._id}")
                json.put("user", "")
                json.put("format", "calling")
                socket.send(json.toString())
            }
        }
        return super.onOptionsItemSelected(item)

    }
   inner class SocketListener(val context: Context) : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response);

        }
        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            Log.d("success", text)
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main){
                    try {
                        var json : JSONObject = JSONObject(text)
                        var idz= json.getString("id")

                        var type = json.getString("format")
                        if(type.toLowerCase().equals("calling") || type.toLowerCase().equals("recieving"))
                        {
                            when(type)
                            {
                                "calling" -> {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        withContext(Main) {
//                                        var dialog = Dialog(context)



                                            dialog.window!!.setLayout(
                                                LinearLayout.LayoutParams.MATCH_PARENT,
                                                LinearLayout.LayoutParams.MATCH_PARENT
                                            )
                                            if (json.getString("id") == id) {
                                                if (json.getString("from") != RetroftiService.users!!._id) {
                                                    dialog.setContentView(R.layout.recieving)
                                                    var background: ImageView =
                                                        dialog.findViewById(R.id.background)
                                                    var username: TextView =
                                                        dialog.findViewById(R.id.username)
                                                    var profile: CircleImageView =
                                                        dialog.findViewById(R.id.profile)

                                                    var calling: TextView =
                                                        dialog.findViewById(R.id.calling)
                                                    var recieve: LottieAnimationView =
                                                        dialog.findViewById(
                                                            R.id.recieve
                                                        )

                                                    CoroutineScope(Dispatchers.IO).launch {
                                                        var response = UserRepository().getUsers(json.getString("from"))
                                                        print(json.getString("from"))
                                                        if (response.success == true) {
                                                            withContext(Main) {
                                                                Glide.with(context).load(
                                                                    RetroftiService.loadImage(
                                                                        response.user!!.Profile!!
                                                                    )
                                                                ).into(profile)
                                                                Glide.with(context).load(
                                                                    RetroftiService.loadImage(
                                                                        response.user!!.Profile!!
                                                                    )
                                                                ).into(background)
                                                                username.text = response.user!!.Username
                                                                calling.text =
                                                                    response.user!!.Username + " wants you to join call"

                                                            }
                                                        }
                                                    }
                                                    vibratePlay()
                                                    dialog.findViewById<LottieAnimationView>(R.id.cancel).setOnClickListener(){
                                                        dialog.cancel()
                                                        mediaPlayer.pause()
                                                    }
                                                    recieve.setOnClickListener() {
                                                        com.xrest.buysell.Activity.vibrator.cancel()
                                                        com.xrest.buysell.Activity.mediaPlayer.pause()
                                                        var json: JSONObject = JSONObject(text)
                                                        json.put("format", "recieving")
                                                        socket.send(json.toString())
                                                        val options =
                                                            JitsiMeetConferenceOptions.Builder()
                                                                .setRoom(id)
                                                                .setWelcomePageEnabled(false)
                                                                .build()
                                                        JitsiMeetActivity.launch(this@MessageActivity, options)
                                                        dialog.cancel()
                                                    }
                                                } else {
                                                    dialog.setContentView(R.layout.calling)
                                                    var background: ImageView =
                                                        dialog.findViewById(R.id.background)
                                                    var username: TextView =
                                                        dialog.findViewById(R.id.username)
                                                    var profile: CircleImageView =
                                                        dialog.findViewById(R.id.profile)
                                                   dialog.findViewById<LottieAnimationView>(R.id.cancel).setOnClickListener(){
                                                       dialog.cancel()
                                                   }
                                                    Glide.with(context).load(
                                                        RetroftiService.loadImage(
                                                            toUser.Profile!!
                                                        )
                                                    ).into(profile)
                                                    Glide.with(context).load(
                                                        RetroftiService.loadImage(
                                                            toUser.Profile!!
                                                        )
                                                    ).into(background)
                                                    username.text = toUser.Username
                                                }
                                                dialog.setCancelable(false)

                                                dialog.show()
                                            }

                                        }
                                    }
                                }
                                "recieving" -> {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        withContext(Main) {
                                            dialog.cancel()
                                            if (json.getString("id") == id) {
                                                val options = JitsiMeetConferenceOptions.Builder()
                                                    .setRoom(id)
                                                    .setWelcomePageEnabled(false)
                                                    .build()
                                                JitsiMeetActivity.launch(this@MessageActivity, options)
                                            }
                                        }
                                    }

                                }
                            }

                        }
                        else{

                            var message= json.getString("message")
                            var userId= json.getString("user")
                            if(idz==id)
                            {

                                if(userId==RetroftiService.users?._id)
                                {
                                    if(type=="Image")
                                    {

                                        adapter.add(
                                            ImageAdapter2(
                                                context, InnerMessage(
                                                    message = message, user = Person(
                                                        _id = RetroftiService.users!!._id,
                                                        Profile = RetroftiService.users!!.Profile
                                                    )
                                                )
                                            )
                                        )
                                        ChatItem2(context, InnerMessage()).notifyChanged()
                                        rv.smoothScrollToPosition(adapter.getItemCount() - 1);

                                    }
                                    else{
                                        adapter.add(
                                            ChatItem2(
                                                context, InnerMessage(
                                                    message = message, user = Person(
                                                        _id = RetroftiService.users!!._id,
                                                        Profile = RetroftiService.users!!.Profile
                                                    )
                                                )
                                            )
                                        )
                                        ChatItem2(context, InnerMessage()).notifyChanged()
                                        rv.smoothScrollToPosition(adapter.getItemCount() - 1);
                                    }

                                }
                                else{
                                    if(type=="Image")
                                    {


                                        adapter.add(
                                            ImageAdapter(
                                                context, InnerMessage(
                                                    message = message, user = Person(
                                                        _id = RetroftiService.users!!._id,
                                                        Profile = RetroftiService.users!!.Profile
                                                    )
                                                )
                                            )
                                        )
                                        ChatItem2(context, InnerMessage()).notifyChanged()
                                        rv.smoothScrollToPosition(adapter.getItemCount() - 1);
                                        ChatItem(context, InnerMessage()).notifyChanged()
                                    }
                                    else{
                                        adapter.add(
                                            ChatItem(
                                                context, InnerMessage(
                                                    message = message, user = Person(
                                                        _id = RetroftiService.users!!._id,
                                                        Profile = RetroftiService.users!!.Profile
                                                    )
                                                )
                                            )
                                        )
                                        ChatItem2(context, InnerMessage()).notifyChanged()
                                        rv.smoothScrollToPosition(adapter.getItemCount() - 1);
                                        ChatItem(context, InnerMessage()).notifyChanged()
                                    }


                                }

                            }
                        }





                        MessageActivity().img=null


                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

        }


        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosing(webSocket, code, reason)
            socket.close(1000, "Bye")
            socket.cancel()
        }

        @SuppressWarnings("MissingPermission")
        fun vibratePlay(){
            com.xrest.buysell.Activity.vibrator = context.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= 26) {
                com.xrest.buysell.Activity.vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        200000,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
                com.xrest.buysell.Activity.mediaPlayer = MediaPlayer.create(context, R.raw.rintone)
                com.xrest.buysell.Activity.mediaPlayer.isLooping = true
                com.xrest.buysell.Activity.mediaPlayer.start()
                Toast.makeText(context, "media playing", Toast.LENGTH_SHORT).show()
            } else {
                com.xrest.buysell.Activity.vibrator.vibrate(200000)
            }
        }

    }
}

