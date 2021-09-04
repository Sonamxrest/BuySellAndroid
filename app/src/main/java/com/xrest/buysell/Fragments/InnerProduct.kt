package com.xrest.buysell.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.xrest.buysell.Adapters.ViewPagerAdapter
import com.xrest.buysell.R
import com.xrest.buysell.Retrofit.Product
import com.xrest.buysell.Retrofit.Repo.ProductRepo
import com.xrest.buysell.Retrofit.RetroftiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.imaginativeworld.whynotimagecarousel.ImageCarousel
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import java.lang.Exception

class InnerProduct : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        var product= arguments?.getSerializable("product") as Product
        var lst = mutableListOf<CarouselItem>()
        for(data in product.Images!!)
        {
            if(data!=null)
            {
                lst.add(CarouselItem(RetroftiService!!.loadImage(data)))

            }
        }
        val view = inflater.inflate(R.layout.fragment_inner_product, container, false)
        var like:CheckBox = view.findViewById(R.id.like)
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
                            RetroftiService.users = response.user!!
                            Toast.makeText(context, "Product Liked & Added to WishList", Toast.LENGTH_SHORT).show()

                        }

                    }
                    else{
                        withContext(Dispatchers.Main)
                        {
                            RetroftiService.users = response.user!!
                            Toast.makeText(context, "Product UnLiked & Removed from WishList", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }catch (ex: Exception){

            }
        }
        var carousel =view.findViewById<ImageCarousel>(R.id.carousel)
        var tab :TabLayout =view.findViewById(R.id.tb)

        carousel.autoPlay=true
        carousel.autoWidthFixing=true
        carousel.showBottomShadow =true
        carousel.addData(lst)
        var title=arrayOf("Description","Comments")
        var fragment =mutableListOf(ProductDescription(product),ProductComment(product._id!!,product.Comments!!))
        var vp2: ViewPager2 = view.findViewById(R.id.vp)
        vp2.adapter= ViewPagerAdapter(fragment,requireFragmentManager(),lifecycle)
        TabLayoutMediator(tab,vp2){tab,position->
tab.setText(title[position])
        }.attach()
        return view
    }


}