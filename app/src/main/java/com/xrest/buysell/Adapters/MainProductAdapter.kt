package com.xrest.buysell.Adapters

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.xrest.buysell.Activity.Dashboard
import com.xrest.buysell.Fragments.InnerProduct
import com.xrest.buysell.R
import com.xrest.buysell.Retrofit.Product
import com.xrest.buysell.Retrofit.Productss
import com.xrest.buysell.Retrofit.Repo.ProductRepo
import com.xrest.buysell.Retrofit.RetroftiService
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.imaginativeworld.whynotimagecarousel.ImageCarousel
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import java.lang.Exception

class MainProductAdapter(var context:Context,var product: Product):Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        var view = viewHolder.itemView
        var lst:MutableList<CarouselItem> = mutableListOf()
        var profile:CircleImageView =view.findViewById(R.id.profile)
        var username: TextView = view.findViewById(R.id.username)
        var carousel:ImageCarousel = view.findViewById(R.id.carousel)
        var like:CheckBox = view.findViewById(R.id.like)
        var name:TextView = view.findViewById(R.id.name)
        var price:TextView = view.findViewById(R.id.price)
        var condition:TextView = view.findViewById(R.id.condition)
        var type:TextView = view.findViewById(R.id.type)
        Glide.with(context).load(RetroftiService.loadImage(product.User!!.Profile!!)).into(profile)
        username.text = product.User!!.Username
        for(data in product.Likes!!)
        {
              if(data == RetroftiService.users!!._id){
                  like.isChecked =true
                  print("${product._id}")
            }
        }
        for(data in product.Images!!)
        {
            lst.add(CarouselItem(imageUrl = RetroftiService.loadImage(data)))
        }

        view.findViewById<CardView>(R.id.card).setOnClickListener(){

var bundle =Bundle()
            bundle.putSerializable("product",product)
            var fragment = InnerProduct()
            fragment.arguments =bundle
            var activity = context as AppCompatActivity
            activity.supportFragmentManager.beginTransaction().replace(R.id.fl,fragment).addToBackStack(null).commit()
        }
        like.setOnClickListener(){
            try {
                CoroutineScope(Dispatchers.IO).launch {
                    var response = ProductRepo().Like(product!!._id!!)
                    if(response.success==true)
                    {
                        withContext(Main)
                        {
                            Toast.makeText(context, "Product Liked & Added to WishList", Toast.LENGTH_SHORT).show()

                        }

                    }
                    else{
                        withContext(Main)
                        {
                            Toast.makeText(context, "Product UnLiked & Removed from WishList", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }catch (ex:Exception){

            }
        }

        carousel.autoPlay=true
        carousel.addData(lst)
        name.text ="Name: "+ product.Name!!
        price.text = "Rs: "+product.Price!!
        condition.text = "Cond:"+product.Condition!!
        type.text = "Cat: "+product.Category!!
    }

    override fun getLayout(): Int {
     return R.layout.main_product_layout
    }
}