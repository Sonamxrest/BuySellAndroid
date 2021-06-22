package com.xrest.buysell

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation.findNavController
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

    }
}




//try {
//    mSocket = IO.socket("http://10.0.2.2:5000")
//    mSocket!!.connect()
//} catch (ex: URISyntaxException) {
//}
//
//mSocket!!.on("message", object : Emitter.Listener {
//    override fun call(vararg args: Any?) {
//        var json = args[0] as JSONObject
//        var message = json.getString("message")
//        CoroutineScope(Dispatchers.IO).launch {
//            withContext(Dispatchers.Main)
//            {
//                Toast.makeText(this@MainActivity, "${message}", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//})