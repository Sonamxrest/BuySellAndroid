package com.xrest.buysell.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xrest.buysell.Adapters.MainProductAdapter
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
    var adapter = GroupAdapter<GroupieViewHolder>()
    var lst:MutableList<Product> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        var userNumber = intent.getStringExtra("number")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                var response = UserRepository().getUser(userNumber!!)
                if(response.success ==  true)
                {
                    user = response.user!!
                    withContext(Main){
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
                        addToAdapter(lst)
                        //dialog.cancel()
                        rv.adapter =adapter
                    }
                }
            }
            catch (ex: Exception)
            {

            }
        }
    }
    fun addToAdapter(lst:MutableList<Product>)
    {
        adapter.clear()
        for(data in lst)
        {
            adapter.add(MainProductAdapter(this,data))
        }
    }
}