package com.xrest.buysell.Adapters

import android.content.Context
import android.content.Intent
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.xrest.buysell.Activity.UserProfile
import com.xrest.buysell.Activity.adapter
import com.xrest.buysell.Fragments.FriendFragment
import com.xrest.buysell.R
import com.xrest.buysell.Retrofit.Class.Request
import com.xrest.buysell.Retrofit.Repo.RequestRepo
import com.xrest.buysell.Retrofit.RetroftiService

import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RequestAdapter(val request: Request, val context: Context):Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        var image: ImageView =viewHolder.itemView.findViewById(R.id.profile)
        var confirm: Button = viewHolder.itemView.findViewById(R.id.confirm)
        var delete:Button = viewHolder.itemView.findViewById(R.id.delete)
        var name: TextView = viewHolder.itemView.findViewById(R.id.name)
        var username = viewHolder.itemView.findViewById(R.id.username) as TextView

        Glide.with(context).load(RetroftiService.loadImage(request.From?.Profile!!)).into(image)
        name.text = request.From?.Name
        username.text = request.From?.Username

        image.setOnClickListener(){
            var intent = Intent(context, UserProfile::class.java)
            intent.putExtra("number",request.From.PhoneNumber)
            context.startActivity(intent)
        }
        confirm.setOnClickListener(){
            CoroutineScope(Dispatchers.IO).launch {
                val response = RequestRepo().acceptReq(request)
                if(response.success==true)
                {
                    withContext(Dispatchers.Main){
                        Toast.makeText(context, "You are now friends", Toast.LENGTH_SHORT).show()
                        notifyChanged()
                        (context as AppCompatActivity).supportFragmentManager.beginTransaction().apply {
                            replace(R.id.fl, FriendFragment())
                            commit()
                        }
                    }
                }

            }


        }
        delete.setOnClickListener(){
            CoroutineScope(Dispatchers.IO).launch {
                val response = RequestRepo().deleteRequest(request._id!!)
                if(response.success==true)
                {
                    withContext(Dispatchers.Main){
                        viewHolder.item.notifyChanged()
                        Toast.makeText(context, "Removed from request", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    override fun getLayout(): Int {
return R.layout.confirm_request
    }


}