package com.xrest.buysell.Adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xrest.buysell.R
import com.xrest.buysell.Retrofit.Person
import com.xrest.buysell.Retrofit.RetroftiService
import de.hdodenhof.circleimageview.CircleImageView

class PayUserAdapter(val context: Context, val lst:MutableList<Person>):RecyclerView.Adapter<PayUserAdapter.PayViewHolder>() {
    class PayViewHolder(view: View):RecyclerView.ViewHolder(view){

        var name:TextView = view.findViewById(R.id.name)
        var rating:RatingBar = view.findViewById(R.id.rate)
        var profile: CircleImageView = view.findViewById(R.id.profile)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PayViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.pay_user_layout,parent,false)
        return PayViewHolder(view)
    }

    override fun onBindViewHolder(holder: PayViewHolder, position: Int) {
     var currentUser =  lst[position]
        if(currentUser!!.Rating!!.size>0)
        {
            var total =0
            for (data in currentUser.Rating!!)
            {
                total += data.rating!!
            }

            holder.rating.rating = (total/currentUser!!.Rating!!.size).toFloat()

        }
        holder.name.text = currentUser.Name!!
       Glide.with(context).load(RetroftiService.loadImage(currentUser.Profile!!)).into(holder.profile)
    }

    override fun getItemCount(): Int {
       return lst.size
    }
}