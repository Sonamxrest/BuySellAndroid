package com.xrest.buysell.Adapters

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.widget.ImageView
import android.widget.LinearLayout

import coil.transform.RoundedCornersTransformation
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.with
import com.makeramen.roundedimageview.RoundedTransformationBuilder
import com.squareup.picasso.Picasso
import com.xrest.buysell.R
import com.xrest.buysell.Retrofit.InnerMessage
import com.xrest.buysell.Retrofit.RetroftiService
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.hdodenhof.circleimageview.CircleImageView

class ImageAdapter2( val context:Context,val message: InnerMessage):Item<GroupieViewHolder>() {
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

        imageView.setOnClickListener(){
            var dialog = Dialog(context)
            dialog.setContentView(R.layout.full_image)
            dialog.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            var image = dialog.findViewById<ImageView>(R.id.image)
            Picasso.get()
                .load(RetroftiService.loadImage(message!!.message!!))
                .fit()
                .transform(transform)
                .into(image);
            dialog.show()
            dialog.setCancelable(true)

        }
        var profile: CircleImageView = viewHolder.itemView.findViewById(R.id.ci)
        Glide.with(context).load(RetroftiService.loadImage(message.user?.Profile!!)).into(profile)

    }
    override fun getLayout(): Int {
      return R.layout.image_right
    }
}