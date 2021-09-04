package com.xrest.buysell.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.xrest.buysell.Adapters.MainProductAdapter
import com.xrest.buysell.Adapters.WishAdapter
import com.xrest.buysell.R
import com.xrest.buysell.Retrofit.Product
import com.xrest.buysell.Retrofit.Repo.ProductRepo
import com.xrest.buysell.Retrofit.Repo.RequestRepo
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
    lateinit var button: Button
    var adapter = GroupAdapter<GroupieViewHolder>()
    var lst:MutableList<Product> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        var cl:ScrollView = findViewById(R.id.container)
        rate = findViewById(R.id.rb)
        button = findViewById(R.id.add)

        button.setOnClickListener(){
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RequestRepo().sendRequest(user._id!!)
                    if (response.success == true) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@UserProfile, "Request Sent Successfully", Toast.LENGTH_SHORT)
                                .show()
                            val snackbar = Snackbar
                                .make(
                                    cl,
                                    "Request Sent Successfully",
                                    Snackbar.LENGTH_LONG
                                )
                            snackbar.show()
                        }
                    }
                    else{
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@UserProfile, "Request is already on users bucket", Toast.LENGTH_SHORT)
                                .show()
                            val snackbar = Snackbar
                                .make(
                                    cl,
                                    "Request is already on users bucket",
                                    Snackbar.LENGTH_LONG
                                )
                            snackbar.show()
                        }
                    }
                } catch (ex: Exception) {

                }
            }
        }
        var userNumber = intent.getStringExtra("number")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                var response = UserRepository().getUser(userNumber!!)
                if(response.success ==  true)
                {
                    user = response.user!!
                    withContext(Main){

                        for(data in user.Friends!!)
                        {
                            if(data.user._id==RetroftiService.users!!._id)
                            {
                                button.isVisible = false
                            }
                        }
            findViewById<TextView>(R.id.name).text = user.Name!!
                        findViewById<TextView>(R.id.review).text = "No. of Reviews: "+user.Rating!!.size.toString()!!
                        if(user!!.Rating!!.size>0)
                        {
                            var total =0
                            for (data in user.Rating!!)
                            {
                                total += data.rating!!
                            }
                            findViewById<TextView>(R.id.ra).text = (total/user!!.Rating!!.size).toFloat().toString()
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
                                        Toast.makeText(this@UserProfile, "Rated Successfully", Toast.LENGTH_SHORT).show()
                                        findViewById<TextView>(R.id.review).text = "No. of Reviews: "+ response.user!!.Rating!!.size.toString()!!
                                            var total =0
                                            for (data in response.user!!.Rating!!)
                                            {
                                                total += data.rating!!
                                            }
                                            findViewById<TextView>(R.id.ra).text = (total/response.user!!.Rating!!.size).toFloat().toString()
                                            rate.rating = (total/response.user!!.Rating!!.size).toFloat()
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
                        var adapter = GroupAdapter<GroupieViewHolder>()

                        rv.adapter = MainProductAdapter(this@UserProfile,lst)
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