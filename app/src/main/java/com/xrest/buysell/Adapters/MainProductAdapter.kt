package com.xrest.buysell.Adapters

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.xrest.buysell.Fragments.InnerProduct
import com.xrest.buysell.Fragments.UpdateProduct
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


class MainProductAdapter(var context:Context,var lst: MutableList<Product>):RecyclerView.Adapter<MainProductAdapter.MVH>(){

    class MVH(view: View):RecyclerView.ViewHolder(view)
    {
        var profile:CircleImageView =view.findViewById(R.id.profile)
var username: TextView = view.findViewById(R.id.username)
var carousel:ImageView = view.findViewById(R.id.carousel)
var like:CheckBox = view.findViewById(R.id.like)
var name:TextView = view.findViewById(R.id.name)
var price:TextView = view.findViewById(R.id.price)
var sold:TextView = view.findViewById(R.id.sold)
var comment:ImageButton = view.findViewById(R.id.comment)
var menu: ImageButton = view.findViewById(R.id.menu)
        var card:CardView = view.findViewById(R.id.card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MVH {
      val view = LayoutInflater.from(context).inflate(R.layout.main_product_layout,parent,false)
        return MVH(view)
    }

    override fun onBindViewHolder(holder: MVH, position: Int) {
    var product = lst[position]
        if(product.SoldOut == true)
        {
            holder.like.isVisible = false
        }
        holder.menu.isVisible = false
        holder.sold.isVisible = false
        holder.comment.setOnClickListener(){
            var dailog = BottomSheetDialog(context)
            dailog.setContentView(R.layout.comment)
            dailog.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            var rv = dailog.findViewById<RecyclerView>(R.id.rv)
            rv!!.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            if(product.Comments?.size!! > 0)
            {
                rv.adapter = CommentAdapter(context , product.Comments!!,product._id!!)

            }
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
            holder.menu.isVisible = true
        }
        holder.menu.setOnClickListener(){
            val popupMenu =PopupMenu(context,holder.menu)
            popupMenu.menuInflater.inflate(R.menu.menutop,popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId)
                {
                    R.id.navUpdate->{

                        (context as AppCompatActivity)!!.supportFragmentManager.beginTransaction().apply {
                            replace(R.id.fl,UpdateProduct(product))
                            commit()
                        }
                    }
                    R.id.navDelete -> {
                        CoroutineScope(Dispatchers.IO).launch {
                            val response = ProductRepo().delete(product._id!!)
                            if (response.success == true) {
                                withContext(Main)
                                {
                                    Toast.makeText(context, "One Item Deleted", Toast.LENGTH_SHORT).show()
                                    lst.removeAt(position)
                                    notifyDataSetChanged()
                                }

                            }
                        }
                    }
                    R.id.navSold->{
                        holder.sold.isVisible = true
                        CoroutineScope(Dispatchers.IO).launch {
                            val response = ProductRepo().sold(product._id!!)
                            if (response.success == true) {
                                withContext(Main)
                                {
                                    Toast.makeText(context, "Item Sold", Toast.LENGTH_SHORT).show()
                                }

                            }
                        }
                    }

                }
                true
            }
            popupMenu.show()
        }
        Glide.with(context).load(RetroftiService.loadImage(product.User!!.Profile!!)).into(holder.profile)
        holder.username.text = product.User!!.Username
        for(data in product.Likes!!)
        {
            if(data == RetroftiService.users!!._id){
                holder.like.isChecked =true
                print("${product._id}")
            }
        }


        holder.card.setOnClickListener(){

            var bundle =Bundle()
            bundle.putSerializable("product",product)
            var fragment = InnerProduct()
            fragment.arguments =bundle
            var activity = context as AppCompatActivity
            activity.supportFragmentManager.beginTransaction().replace(R.id.fl,fragment).addToBackStack(null).commit()
        }
        holder.like.setOnClickListener(){
            try {
                CoroutineScope(Dispatchers.IO).launch {
                    var response = ProductRepo().Like(product!!._id!!)
                    if(response.success==true)
                    {
                        withContext(Main)
                        {
                            Toast.makeText(context, "Product Liked & Added to WishList", Toast.LENGTH_SHORT).show()
                            RetroftiService.users = response.user!!

                        }

                    }
                    else{
                        withContext(Main)
                        {
                            RetroftiService.users = response.user!!
                            Toast.makeText(context, "Product UnLiked & Removed from WishList", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }catch (ex:Exception){

            }
        }
        if(product.SoldOut === true)
        {
            holder.sold.isVisible = true;
        }
        Log.d("mmm",RetroftiService.loadImage(product.Images?.get(0)!!))
        Glide.with(context).load(RetroftiService.loadImage(product.Images?.get(0)!!)).into(holder.carousel)
        holder.name.text ="Name: "+ product.Name!!
        holder.price.text = "Rs: "+product.Price!!
    }

    override fun getItemCount(): Int {
      return lst.size
    }


}