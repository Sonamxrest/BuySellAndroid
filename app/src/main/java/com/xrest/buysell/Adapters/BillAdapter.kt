package com.xrest.buysell.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xrest.buysell.R
import com.xrest.buysell.Retrofit.Class.Transaction

class BillAdapter(var context: Context, var lst:MutableList<Transaction>):RecyclerView.Adapter<BillAdapter.BillViewHolder>() {
    class BillViewHolder(view: View):RecyclerView.ViewHolder(view){
        var date :TextView = view.findViewById(R.id.date)
        var pname :TextView = view.findViewById(R.id.pname)
        var price:TextView = view.findViewById(R.id.price)
        var amountPaid:TextView = view.findViewById(R.id.amtpaid)
        var username:TextView = view.findViewById(R.id.username)
        var description:TextView = view.findViewById(R.id.desc)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.bill,parent,false)
        return BillViewHolder(view)
    }

    override fun onBindViewHolder(holder: BillViewHolder, position: Int) {
        var transaction = lst[position]
        holder.date.text = transaction.Date.toString()
        holder.pname.text = transaction.Product!!.Name
        holder.amountPaid.text = transaction.Amount!!.toString()
        holder.username.text = transaction.Reciever!!.Name
        holder.description.text = transaction.Description
        holder.price.text = transaction.Product!!.Price!!
    }

    override fun getItemCount(): Int {
      return lst.size
    }
}