package com.xrest.buysell.Adapters

import android.graphics.Color
import android.widget.ImageView
import com.makeramen.roundedimageview.RoundedTransformationBuilder
import com.squareup.picasso.Picasso
import com.xrest.buysell.R
import com.xrest.buysell.Retrofit.InnerMessage
import com.xrest.buysell.Retrofit.RetroftiService

import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class ImageAdapter(val message: InnerMessage): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
    val imageView = viewHolder.itemView.findViewById(R.id.img) as ImageView
    val transform = RoundedTransformationBuilder().borderColor(Color.BLACK)
        .borderWidthDp(3f)
        .cornerRadiusDp(30f)
        .oval(false)
        .build();
    Picasso.get()
        .load(RetroftiService.loadImage(message!!.message!!))
        .fit()
        .transform(transform)
        .into(imageView);
}

    override fun getLayout(): Int {
        return R.layout.image_left
    }
}