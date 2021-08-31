package com.xrest.buysell.Adapters

import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.xrest.buysell.Fragments.InnerProduct
import com.xrest.buysell.R
import com.xrest.buysell.Retrofit.Product
import com.xrest.buysell.Retrofit.Repo.ProductRepo
import com.xrest.buysell.Retrofit.RetroftiService
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception


class ProductShowAdapter(val context: Context, val product:Product): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        var image: ImageView = viewHolder.itemView.findViewById(R.id.image)
        var price:TextView = viewHolder.itemView.findViewById(R.id.price)
        var name:TextView = viewHolder.itemView.findViewById(R.id.name)
        var like : CheckBox = viewHolder.itemView.findViewById(R.id.like)
        viewHolder.itemView.findViewById<LinearLayout>(R.id.container).setOnClickListener(){
            var bundle = Bundle()
            bundle.putSerializable("product",product)
            var fragment = InnerProduct()
            fragment.arguments =bundle
            var activity = context as AppCompatActivity
            activity.supportFragmentManager.beginTransaction().replace(R.id.fl,fragment).addToBackStack(null).commit()
        }
        for(data in RetroftiService.users!!.Likes!!)
        {
            if(data.product!!._id!!.equals(product._id))
            {
                like.isChecked = true
            }
        }
        like.setOnClickListener(){
            try {
                CoroutineScope(Dispatchers.IO).launch {
                    var response = ProductRepo().Like(product!!._id!!)
                    if(response.success==true)
                    {
                        withContext(Dispatchers.Main)
                        {
                            Toast.makeText(context, "Product Liked & Added to WishList", Toast.LENGTH_SHORT).show()

                        }

                    }
                    else{
                        withContext(Dispatchers.Main)
                        {
                            Toast.makeText(context, "Product UnLiked & Removed from WishList", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }catch (ex: Exception){

            }
        }
        price.text = "Rs "+product.Price
        name.text =product.Name

        Glide.with(context).load(RetroftiService.loadImage(product.Images?.get(0)!!)).into(image)

    }

    override fun getLayout(): Int {
        return R.layout.product
    }
}