package com.xrest.buysell.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.ContentInfoCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.xrest.buysell.Fragments.*
import com.xrest.buysell.R
import com.xrest.buysell.Retrofit.RetroftiService
import de.hdodenhof.circleimageview.CircleImageView



class Dashboard : AppCompatActivity() {
    lateinit var toggle:ActionBarDrawerToggle
    lateinit var drawer:DrawerLayout
   // lateinit var nav:NavController
    lateinit var navigationView:NavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        drawer = findViewById(R.id.container)
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
                    currentFrag(FriendFragment())
                     //Navigation.findNavController(this, R.id.fl).navigate(R.id.action_home2_to_addPost)
                    //navigationView.menu.getItem(1).isEnabled =false

                    Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show()
                }
                R.id.profile ->{
                    currentFrag(Profile())
                }
                R.id.home ->{
                    currentFrag(WishList())
                }
                R.id.admin ->{
                    currentFrag(AllPost())
                }
                R.id.order->{
                    currentFrag(ShowUsers())
                }
                R.id.logout->{
//                    var intent = Intent(this@Dashboard,MainActivity::class.java);
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.and(Intent.FLAG_ACTIVITY_NEW_TASK)
//                  //  Navigation.findNavController(MainActivity().v).navigate(R.id.action_splash_to_startActions)
//                    startActivity(intent)
                    var intent = Intent(this@Dashboard,CallCheckActivity::class.java);
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.and(Intent.FLAG_ACTIVITY_NEW_TASK)
                    //  Navigation.findNavController(MainActivity().v).navigate(R.id.action_splash_to_startActions)
                    startActivity(intent)
                }
                R.id.request->{
                    currentFrag(FriendRequest())
                }

            }
            true
        }

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
}