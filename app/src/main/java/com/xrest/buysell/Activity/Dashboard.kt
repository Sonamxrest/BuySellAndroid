package com.xrest.buysell.Activity

import android.app.Dialog
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.ContentInfoCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.xrest.buysell.Adapters.ChatItem
import com.xrest.buysell.Adapters.ChatItem2
import com.xrest.buysell.Adapters.ImageAdapter
import com.xrest.buysell.Adapters.ImageAdapter2
import com.xrest.buysell.Fragments.*
import com.xrest.buysell.R
import com.xrest.buysell.Retrofit.InnerMessage
import com.xrest.buysell.Retrofit.Person
import com.xrest.buysell.Retrofit.Repo.UserRepository
import com.xrest.buysell.Retrofit.RetroftiService
import com.xrest.buysell.Retrofit.User
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import org.json.JSONException
import org.json.JSONObject


class Dashboard : AppCompatActivity() {
    lateinit var toggle:ActionBarDrawerToggle
    lateinit var drawer:DrawerLayout
   // lateinit var nav:NavController
    lateinit var navigationView:NavigationView
    lateinit var socket:WebSocket
    lateinit var toUser: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        val url="ws://10.0.2.2:5000"
        var okHttpClient = OkHttpClient()
        val request= Request.Builder().url(url).build()
        socket = okHttpClient.newWebSocket(request, SocketListener(applicationContext))
        drawer = findViewById(R.id.dl)
        toggle = ActionBarDrawerToggle(this,drawer, R.string.open, R.string.close)
        toggle.syncState()
       // setSupportActionBar(findViewById(R.id.my_toolbar))
      //nav = Navigation.findNavController(this,R.id.fl)
         navigationView = findViewById<NavigationView>(R.id.navView)
        var header = navigationView.getHeaderView(0)
        currentFrag(Home())
        var cp:CircleImageView = header.findViewById(R.id.profile)
        var name:TextView = header.findViewById(R.id.name)
        name.text = RetroftiService.users!!.Name
        Glide.with(this).load("${RetroftiService.BASE_URL}uploads/${RetroftiService.users!!.Profile}").into(cp)
        drawer.addDrawerListener(toggle)
//        //supportActionBar!!.setBackgroundDrawable( ColorDrawable(Color.parseColor("#0277BD")));
//        supportActionBar!!.setDisplayShowTitleEnabled(false);
//        supportActionBar!!.setDisplayShowTitleEnabled(true);
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        navigationView.setNavigationItemSelectedListener{
            when(it.itemId)
            {
                R.id.add -> {
                    currentFrag(AddPost())
                     //Navigation.findNavController(this, R.id.fl).navigate(R.id.action_home2_to_addPost)
                    //navigationView.menu.getItem(1).isEnabled =false

                    Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show()
                }
                R.id.profile ->{
                    currentFrag(Profile())
                }
                R.id.home ->{
                    currentFrag(Home())
                }
                R.id.admin ->{
                    currentFrag(AllPost())
                }
                R.id.wish->{
                    currentFrag(WishList())
                }
                R.id.order->{
                    currentFrag(ShowUsers())
                }
                R.id.logout->{
//                    var intent = Intent(this@Dashboard,MainActivity::class.java);
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.and(Intent.FLAG_ACTIVITY_NEW_TASK)
//                  //  Navigation.findNavController(MainActivity().v).navigate(R.id.action_splash_to_startActions)
//                    startActivity(intent)
//                    var intent = Intent(this@Dashboard,CallCheckActivity::class.java);
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.and(Intent.FLAG_ACTIVITY_NEW_TASK)
//                    //  Navigation.findNavController(MainActivity().v).navigate(R.id.action_splash_to_startActions)
//                    startActivity(intent)
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            var response = UserRepository().logout()
                            if(response.success == true)
                            {
                                withContext(Main){
                                    currentFrag(LoginFragment())

                                }
                            }
                        }
                        catch (ex: Exception){
                            ex.printStackTrace()
                        }
                    }
                }
                R.id.request->{
                    currentFrag(FriendRequest())
                }
R.id.chat->{
    currentFrag(FriendFragment())
}
            }
            true
        }

    }

    override fun onResume() {
        super.onResume()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item))
        { return true
        }
        when(item.itemId)
        {
            R.id.profile -> {
                currentFrag(Profile())

            }
        }
        return super.onOptionsItemSelected(item)

    }


    fun currentFrag(frag:Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl,frag)
            commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.side_nav,menu)
        return super.onCreateOptionsMenu(menu)
    }


    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
currentFrag(Home())
        Handler(Looper.getMainLooper()).postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
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


                                            var dialog = Dialog(this@Dashboard)
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
                                                    }
                                                    recieve.setOnClickListener() {
                                                       vibrator.cancel()
                                                      mediaPlayer.pause()
                                                        var json: JSONObject = JSONObject(text)
                                                        json.put("format", "recieving")
                                                        socket.send(json.toString())
                                                        val options =
                                                            JitsiMeetConferenceOptions.Builder()
                                                                .setRoom(id)
                                                                .setWelcomePageEnabled(false)
                                                                .build()
                                                        JitsiMeetActivity.launch(this@Dashboard, options)
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
                                            if (json.getString("id") == id) {
                                                val options = JitsiMeetConferenceOptions.Builder()
                                                    .setRoom(id)
                                                    .setWelcomePageEnabled(false)
                                                    .build()
                                                JitsiMeetActivity.launch(this@Dashboard, options)
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
        lateinit var mediaPlayer: MediaPlayer
        lateinit var vibrator: Vibrator
        @SuppressWarnings("MissingPermission")
        fun vibratePlay(){
           vibrator = context.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= 26) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        200000,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
                mediaPlayer = MediaPlayer.create(context, R.raw.rintone)
              mediaPlayer.isLooping = true
                mediaPlayer.start()
                Toast.makeText(context, "media playing", Toast.LENGTH_SHORT).show()
            } else { vibrator.vibrate(200000)
            }
        }

    }
}