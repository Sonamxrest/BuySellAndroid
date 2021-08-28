package com.xrest.buysell.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RatingBar
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.xrest.buysell.Adapters.MainProductAdapter
import com.xrest.buysell.Adapters.WishAdapter
import com.xrest.buysell.R
import com.xrest.buysell.Retrofit.Product
import com.xrest.buysell.Retrofit.Repo.ProductRepo
import com.xrest.buysell.Retrofit.Repo.UserRepository
import com.xrest.buysell.Retrofit.RetroftiService
import com.xrest.buysell.Retrofit.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class UserProfile : AppCompatActivity() {
    lateinit var name: TextView
    lateinit var number: TextView
    lateinit var username: TextView
    lateinit var profile: CircleImageView
    lateinit var rv: RecyclerView
    lateinit var user: User
    lateinit var rate: RatingBar
    lateinit var pcount : TextView
    var adapter = GroupAdapter<GroupieViewHolder>()
    var lst:MutableList<Product> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        var cl:ScrollView = findViewById(R.id.container)
        rate = findViewById(R.id.rb)
        var userNumber = intent.getStringExtra("number")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                var response = UserRepository().getUser(userNumber!!)
                if(response.success ==  true)
                {
                    user = response.user!!
                    withContext(Main){

            findViewById<TextView>(R.id.name).text = user.Name!!
                        if(user!!.Rating!!.size>0)
                        {
                            var total =0
                            for (data in user.Rating!!)
                            {
                                total += data.rating!!
                            }
                            findViewById<TextView>(R.id.ra).text = (total/user!!.Rating!!.size).toString()
                            rate.rating = (total/user!!.Rating!!.size).toFloat()

                        }
                        else{
                            rate.rating = 0f

                        }
                        rate.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->


                            CoroutineScope(Dispatchers.IO).launch {
                                val response = UserRepository().rate(user._id!!,rating.toString())
                                if(response.success==true)
                                {
                                    withContext(Main)
                                    {
                                        val snackbar = Snackbar
                                            .make(
                                                cl,
                                                "User Rated Successfully",
                                                Snackbar.LENGTH_LONG
                                            )
                                        snackbar.show()
                                        Toast.makeText(this@UserProfile, "Rated Successfullt", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                        var profile :CircleImageView = findViewById(R.id.profile)
                        Glide.with(applicationContext).load(RetroftiService.loadImage(user.Profile!!)).into(profile)
                        var name: TextView = findViewById(R.id.name)
                        name.text = user.Name
                        var number: TextView = findViewById(R.id.number)
                        number.text = user.PhoneNumber

                        var username: TextView = findViewById(R.id.username)
                        username.text = user.Username
                        rv = findViewById(R.id.rv)
                        rv.layoutManager = LinearLayoutManager(this@UserProfile)
                        loadData()
                    }
                }
            }
            catch (ex: Exception){

            }

        }


    }

    private fun loadData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                var response = ProductRepo().personsPost(user!!._id!!)
                if(response.success==true)
                {
                    withContext(Dispatchers.Main)
                    {
                        lst = response.data!!
                        pcount = findViewById(R.id.totalProduct)
                        pcount.text = lst.size.toString()
                        //dialog.cancel()
                        rv.adapter =WishAdapter(lst,this@UserProfile)
                    }
                }
            }
            catch (ex: Exception)
            {

            }
        }
    }

    override fun onResume() {
        supportActionBar!!.hide()
        super.onResume()
    }
}