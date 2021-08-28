package com.xrest.buysell.Adapters

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.xrest.buysell.Activity.Dashboard
import com.xrest.buysell.Activity.mediaPlayer
import com.xrest.buysell.Fragments.InnerProduct
import com.xrest.buysell.R
import com.xrest.buysell.Retrofit.*
import com.xrest.buysell.Retrofit.Repo.ProductRepo
import com.xrest.buysell.Retrofit.Routes.Products
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
        var profile:CircleImageView =view.findViewById(R.id.profile)
        var username: TextView = view.findViewById(R.id.username)
        var carousel:ImageView = view.findViewById(R.id.carousel)
        var like:CheckBox = view.findViewById(R.id.like)
        var name:TextView = view.findViewById(R.id.name)
        var price:TextView = view.findViewById(R.id.price)
        var sold:TextView = view.findViewById(R.id.sold)
        var comment:ImageButton = view.findViewById(R.id.comment)
        var menu: ImageButton = viewHolder.itemView.findViewById(R.id.menu)
        menu.isVisible = false
        sold.isVisible = false
comment.setOnClickListener(){
    var dailog = BottomSheetDialog(context)
    dailog.setContentView(R.layout.comments)
    dailog.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
    var rv = dailog.findViewById<RecyclerView>(R.id.rv)
    rv!!.layoutManager = LinearLayoutManager(context)
    rv.adapter = CommentAdapter(context , product.Comments!!,product._id!!)
    var edt = dailog.findViewById<EditText>(R.id.editText)
    var button = dailog.findViewById<Button>(R.id.comment)
    button!!.setOnClickListener(){
        CoroutineScope(Dispatchers.IO).launch {
            var person = RetroftiService.users
            try{
                CoroutineScope(Dispatchers.IO).launch {
                    val reposen = ProductRepo().comment(Comment(_id = product._id,comment = edt!!.text.toString()))
                    if(reposen.success==true)
                    {
                        withContext(Dispatchers.Main)
                        {
                            product.Comments!!.add(
                                Comment(user= Person(_id= person!!._id,Name=person.Name,Username = person.Username,Profile = person.Profile),comment = edt.text.toString())
                            )

                            dailog.cancel()

                        }



                    }

                }
            }
            catch (ex: Exception){

            }
        }
    }
    var cancel = dailog.findViewById<Button>(R.id.cancel)
    cancel!!.setOnClickListener(){
        dailog.cancel()
    }
    dailog.show()
    dailog.setCancelable(true)
}
        if(product.User?._id == RetroftiService.users?._id)
        {
            menu.isVisible = true
        }
        menu.setOnClickListener(){
            val popupMenu =PopupMenu(context,menu)
            popupMenu.menuInflater.inflate(R.menu.menutop,popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId)
                {
                    R.id.navUpdate->{

//                        popUpForm()
                    }
                    R.id.navDelete -> {
                        CoroutineScope(Dispatchers.IO).launch {
                            val response = ProductRepo().delete(product._id!!)
                            if (response.success == true) {
                                withContext(Main)
                                {
                                    Toast.makeText(context, "One Item Deleted", Toast.LENGTH_SHORT).show()
                                    notifyChanged()
                                }

                            }
                        }
                    }
                    R.id.navSold->{
                        CoroutineScope(Dispatchers.IO).launch {
                            val response = ProductRepo().sold(product._id!!)
                            if (response.success == true) {
                                withContext(Main)
                                {
                                    Toast.makeText(context, "Item Sold", Toast.LENGTH_SHORT).show()
                                    notifyChanged()
                                }

                            }
                        }
                    }

                }
                true
            }
            popupMenu.show()
        }
        Glide.with(context).load(RetroftiService.loadImage(product.User!!.Profile!!)).into(profile)
        username.text = product.User!!.Username
        for(data in product.Likes!!)
        {
              if(data == RetroftiService.users!!._id){
                  like.isChecked =true
                  print("${product._id}")
            }
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
        if(product.SoldOut === true)
        {
            sold.isVisible = true;
        }
       Glide.with(context).load(RetroftiService.loadImage(product.Images?.get(0)!!)).into(carousel)
        name.text ="Name: "+ product.Name!!
        price.text = "Rs: "+product.Price!!
    }



    override fun getLayout(): Int {
     return R.layout.main_product_layout
    }
}