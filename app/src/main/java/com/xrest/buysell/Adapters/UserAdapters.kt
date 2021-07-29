package com.xrest.buysell.Adapters
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xrest.buysell.R
import com.xrest.buysell.Retrofit.Person
import com.xrest.buysell.Retrofit.RetroftiService
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapters(var lst:MutableList<Person>, var context: Context):RecyclerView.Adapter<UserAdapters.Holder>() {

    class Holder(view: View):RecyclerView.ViewHolder(view){

             var profile:CircleImageView = view.findViewById(R.id.imageView)
             var name:TextView = view.findViewById(R.id.name)
             var username:TextView = view.findViewById(R.id.username)
             var add:ImageButton = view.findViewById(R.id.add)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
   var view = LayoutInflater.from(context).inflate(R.layout.friends,parent,false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
   var user = lst[position]
        Glide.with(context).load(RetroftiService.loadImage(user.Profile!!)).into(holder.profile)
        holder.name.text = user.Name
        holder.username.text = user.Username
    }

    override fun getItemCount(): Int {
       return lst.size
    }

}