package com.xrest.buysell.Adapters

import android.R.attr.data
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xrest.buysell.R
import com.xrest.buysell.Retrofit.Productss
import com.xrest.buysell.Retrofit.Repo.ProductRepo
import com.xrest.buysell.Retrofit.RetroftiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception


class WishListAdapter(var lst: MutableList<Productss>, var context: Context):RecyclerView.Adapter<WishListAdapter.WVH>() {

    class WVH(view: View):RecyclerView.ViewHolder(view){
        var image:ImageView = view.findViewById(R.id.image)
        var name:TextView = view.findViewById(R.id.name)
        var price:TextView = view.findViewById(R.id.price)
        var cat:TextView = view.findViewById(R.id.category)
         var delete:CheckBox = view.findViewById(R.id.delete)
        var like:TextView = view.findViewById(R.id.likeCount)

        var comment:TextView = view.findViewById(R.id.commentCount)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WVH {
   var view = LayoutInflater.from(context).inflate(R.layout.newwishlist, parent, false)
        return  WVH(view)
    }

    override fun onBindViewHolder(holder: WVH, position: Int) {
        var product =lst[position]
        if(product!!.product!!.Images!!.size > 0)
        {
            Glide.with(context).load(RetroftiService.loadImage(product!!.product!!.Images?.get(0)!!)).into(
                holder.image
            )
        }
        else{
            Glide.with(context).load(RetroftiService.loadImage("no-image.jpg")).into(
                holder.image
            )
        }

        holder.name.text = product.product?.Name
        holder.price.text = "Rs "+ product.product?.Price
        holder.cat.text = product.product?.Category
        holder.like.text = product.product?.Likes?.size.toString()
        holder.comment.text = product.product?.Comments?.size.toString()

    }


    override fun getItemCount(): Int {
      return lst.size
    }

    fun removeItem(position: Int) {
        like(position)
        notifyItemRemoved(position)

    }

    fun restoreItem(item: Productss, position: Int) {
        lst.add(position, item)
        like(position)

        notifyItemInserted(position)
    }

    fun getData(): MutableList<Productss> {
        return lst
    }
    fun like(position: Int)
    {
   Log.d("position", lst.toString())
        try {
            CoroutineScope(Dispatchers.IO).launch {
                var response = ProductRepo().Like(lst[position].product!!._id!!)
                if(response.success==true)
                {
                    withContext(Dispatchers.Main)
                    {
                        lst.removeAt(position)
                        Toast.makeText(context, "Product Liked & Added to WishList", Toast.LENGTH_SHORT).show()

                    }

                }
                else{
                    withContext(Dispatchers.Main)
                    {
                        lst.removeAt(position)
                        Toast.makeText(context, "Product UnLiked & Removed from WishList", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }catch (ex: Exception){

        }
    }
}