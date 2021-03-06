package com.xrest.buysell.Adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xrest.buysell.Activity.UserProfile
import com.xrest.buysell.Fragments.ShowUsers
import com.xrest.buysell.R
import com.xrest.buysell.Retrofit.Person
import com.xrest.buysell.Retrofit.Repo.RequestRepo
import com.xrest.buysell.Retrofit.RetroftiService
import de.hdodenhof.circleimageview.CircleImageView
import io.socket.client.IO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserAdapters(var lst: MutableList<Person>, var context: Context) :
    RecyclerView.Adapter<UserAdapters.Holder>() {
    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        var profile: CircleImageView = view.findViewById(R.id.imageView)
        var name: TextView = view.findViewById(R.id.name)
        var username: TextView = view.findViewById(R.id.username)
        var add: ImageButton = view.findViewById(R.id.add)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        var view = LayoutInflater.from(context).inflate(R.layout.friends, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        var user = lst[position]
       holder.profile.setOnClickListener(){
           var intent = Intent(context,UserProfile::class.java)
           intent.putExtra("number",user.PhoneNumber)
           context.startActivity(intent)
       }
        Glide.with(context).load(RetroftiService.loadImage(user.Profile!!)).into(holder.profile)
        holder.name.text = user.Name
        holder.username.text = user.Username
        for(data in RetroftiService.users!!.Friends!!)
        {
            if(data.user._id == user._id)
            {
                holder.add.isVisible = false
            }
        }
        holder.add.setOnClickListener() {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RequestRepo().sendRequest(user._id!!)
                    print(response.toString())
                    if (response.success == true) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Request Sent Successfully", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    else{
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Request is already on users bucket", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                } catch (ex: Exception) {
ex.printStackTrace()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return lst.size
    }

}