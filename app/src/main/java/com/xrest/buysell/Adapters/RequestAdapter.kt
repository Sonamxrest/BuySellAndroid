package com.xrest.buysell.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xrest.buysell.Activity.UserProfile
import com.xrest.buysell.Activity.adapter
import com.xrest.buysell.Fragments.FriendFragment
import com.xrest.buysell.R
import com.xrest.buysell.Retrofit.Class.Request
import com.xrest.buysell.Retrofit.Repo.RequestRepo
import com.xrest.buysell.Retrofit.Repo.UserRepository
import com.xrest.buysell.Retrofit.RetroftiService

import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RequestAdapter(val lst: MutableList<Request>, val context: Context):RecyclerView.Adapter<RequestAdapter.VH>() {
class VH(view: View):RecyclerView.ViewHolder(view)
{
        var image: ImageView =view.findViewById(R.id.profile)
    var confirm: Button = view.findViewById(R.id.confirm)
    var delete:Button = view.findViewById(R.id.delete)
    var name: TextView = view.findViewById(R.id.name)
    var username = view.findViewById(R.id.username) as TextView
}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
    val view = LayoutInflater.from(context).inflate(R.layout.confirm_request,parent,false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        var request = lst[position]


        Glide.with(context).load(RetroftiService.loadImage(request.From?.Profile!!)).into(holder.image)
        holder.name.text = request.From?.Name
        holder.username.text = request.From?.Username

        holder.image.setOnClickListener(){
            var intent = Intent(context, UserProfile::class.java)
            intent.putExtra("number", request.From!!.PhoneNumber)
            context.startActivity(intent)
        }
        holder.confirm.setOnClickListener(){
            CoroutineScope(Dispatchers.IO).launch {
                val response = RequestRepo().acceptReq(request)
                if(response.success==true)
                {
                    withContext(Dispatchers.Main){
                        lst.removeAt(position)
notifyDataSetChanged()
                        RetroftiService.users = response.user!!
                        Toast.makeText(context, "You are now friends", Toast.LENGTH_SHORT).show()


                    }
                }

            }


        }
        holder.delete.setOnClickListener(){
            CoroutineScope(Dispatchers.IO).launch {
                val response = RequestRepo().deleteRequest(request._id!!)
                if(response.success==true)
                {
                    withContext(Dispatchers.Main){
                       lst.removeAt(position)
                        notifyDataSetChanged()
                        Toast.makeText(context, "Removed from request", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return lst.size
    }

}

//}