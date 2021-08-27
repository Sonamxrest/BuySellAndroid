package com.xrest.buysell.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xrest.buysell.Adapters.ProductShowAdapter
import com.xrest.buysell.R
import com.xrest.buysell.Retrofit.Product
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import org.imaginativeworld.whynotimagecarousel.ImageCarousel
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem


class Home : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view =  inflater.inflate(R.layout.fragment_home, container, false)
        val carousel: ImageCarousel = view.findViewById(R.id.carousel)
        val rv:RecyclerView = view.findViewById(R.id.rv)
        val list = mutableListOf<CarouselItem>()
        var lst:MutableList<Product> = mutableListOf()
        lst.add(Product(Name = "Iphone Blue", Price = "500",Images = mutableListOf("https://static.toiimg.com/photo/71396840.cms")))
        lst.add(Product(Name = "PS4", Price = "500",Images = mutableListOf("https://static.toiimg.com/thumb/msid-80594781,width-1200,height-900,resizemode-4/.jpg")))
        lst.add(Product(Name = "House", Price = "500",Images = mutableListOf("https://d34ad2g4hirisc.cloudfront.net/volunteer_positions/photos/000/021/198/original/ae61d4401e73a913f8a876bfbac3653e.jpg")))
        lst.add(Product(Name = "ACER", Price = "500",Images = mutableListOf("https://cdn.mos.cms.futurecdn.net/6t8Zh249QiFmVnkQdCCtHK.jpg")))
        lst.add(Product(Name = "TAB", Price = "500",Images = mutableListOf("https://i.gadgets360cdn.com/products/large/amazon-fire-hd-10-kids-pro-623x800-1619599486.jpg")))
        lst.add(Product(Name = "Sofa", Price = "500",Images = mutableListOf("https://www.ulcdn.net/opt/www.ulcdn.net/images/products/94259/original/Apollo_Infinite_FNSF51APDU30000SAAAA_slide_00.jpg?1467963845")))
        lst.add(Product(Name = "KTM RC", Price = "500",Images = mutableListOf("https://images.unsplash.com/photo-1449426468159-d96dbf08f19f?ixid=MnwxMjA3fDB8MHxzZWFyY2h8NXx8bW90b3JiaWtlfGVufDB8fDB8fA%3D%3D&ixlib=rb-1.2.1&w=1000&q=80")))
        lst.add(Product(Name = "Harley", Price = "500",Images = mutableListOf("https://images.unsplash.com/photo-1558981403-c5f9899a28bc?ixid=MnwxMjA3fDB8MHxzZWFyY2h8M3x8YmlrZXN8ZW58MHx8MHx8&ixlib=rb-1.2.1&w=1000&q=80")))
        lst.add(Product(Name = "MT", Price = "500",Images = mutableListOf("https://5.imimg.com/data5/QY/UJ/OZ/SELLER-20896370/yamaha-bike-mt-15-500x500.png")))

        rv.layoutManager =GridLayoutManager(requireContext(),3,GridLayoutManager.VERTICAL,false)
        var adapter = GroupAdapter<GroupieViewHolder>()
        for(data in lst)
        {
            adapter.add(ProductShowAdapter(requireContext(),data))
        }
        carousel.registerLifecycle(lifecycle)
        list.add(
            CarouselItem(
                imageUrl = "https://image.freepik.com/free-vector/sale-banner-template_79298-60.jpg",
                caption = "Photo by Aaron Wu on Unsplash"
            )
        )
        val headers = mutableMapOf<String, String>()
        headers["header_key"] = "header_value"

        list.add(
            CarouselItem(
                imageUrl = "https://i.pinimg.com/originals/c7/28/58/c72858992482c70d5ec3a585eec352ed.png",
                headers = headers
            )
        )


        list.add(
            CarouselItem(
                imageUrl = "https://debock.advertroindia.co.in/uploads-advertro-03-08-2019/debock/bg/debock3.jpg"
            )
        )
        carousel.setData(list)
        rv.adapter = adapter


        return view
    }
    override fun onResume() {

        var actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar!!.show()
        super.onResume()
    }
}