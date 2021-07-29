package com.xrest.buysell.Adapters

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.xrest.buysell.R
import com.xrest.buysell.Retrofit.Product
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item


class ProductShowAdapter(val context: Context, val product:Product): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        var image: ImageView = viewHolder.itemView.findViewById(R.id.image)
        var price:TextView = viewHolder.itemView.findViewById(R.id.price)
        var name:TextView = viewHolder.itemView.findViewById(R.id.name)
        price.text = "Rs "+product.Price
        name.text =product.Name
        Glide.with(context).load("${product.Images!![0]}").into(image)

    }

    override fun getLayout(): Int {
        return R.layout.product
    }
}