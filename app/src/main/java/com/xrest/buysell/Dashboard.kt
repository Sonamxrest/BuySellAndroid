package com.xrest.buysell

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.xrest.buysell.Retrofit.HandleApiReuest
import com.xrest.buysell.Retrofit.RetroftiService
import de.hdodenhof.circleimageview.CircleImageView


class Dashboard : AppCompatActivity() {
    lateinit var toggle:ActionBarDrawerToggle
    lateinit var drawer:DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        drawer = findViewById(R.id.container)
        toggle = ActionBarDrawerToggle(this,drawer,R.string.open,R.string.close)
        toggle.syncState()
        var navigationView = findViewById<NavigationView>(R.id.navView)
        var header = navigationView.getHeaderView(0)
        var cp:CircleImageView = header.findViewById(R.id.profile)
        var name:TextView = header.findViewById(R.id.name)
        name.text = RetroftiService.users!!.Name
       Glide.with(this).load("${RetroftiService.BASE_URL}uploads/${RetroftiService.users!!.Profile}").into(cp)
        navigationView.setNavigationItemSelectedListener {
            when(it.itemId)
            {
                R.id.add->{
                    navigate(AddPost())
                    Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show()

                }
            }
            true
        }

        drawer.addDrawerListener(toggle!!)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item))
        { return toggle.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)

    }
    fun navigate(fragment: Fragment){
        Navigation.findNavController(this,R.id.dashboard_graph).navigate(R.id.action_addPost_to_home2)
    }
}