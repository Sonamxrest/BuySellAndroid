package com.xrest.buysell.Adapters

import android.content.Context
import android.content.Intent
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.xrest.buysell.R
import com.xrest.buysell.Retrofit.InnerMessage
import com.xrest.buysell.Retrofit.RetroftiService

import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class ChatItem(val context: Context, val innerMessage: InnerMessage): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val txt: TextView = viewHolder.itemView.findViewById(R.id.message)
        val image: ImageView = viewHolder.itemView.findViewById(R.id.circleImageView)
//image.setOnClickListener()
//{
//    val intent = Intent(context, UserProfile::class.java)
//    intent.putExtra("user",innerMessage.user!!)
//    context.startActivity(intent)
//}
        txt.text = innerMessage.message
       //Glide.with(context).load(RetroftiService.loadImage(innerMessage.user!!.Profile!!)).into(image)



    }

    override fun getLayout(): Int {
        return R.layout.chat_left
    }

}
