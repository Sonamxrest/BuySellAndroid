package com.xrest.buysell

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.xrest.buysell.Adapters.ProductShowAdapter
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
        lst.add(Product(Name = "Soname", Price = "500",Images = mutableListOf("https://images.unsplash.com/photo-1532581291347-9c39cf10a73c?w=1080")))
        lst.add(Product(Name = "Soname", Price = "500",Images = mutableListOf("https://images.unsplash.com/photo-1534447677768-be436bb09401?w=1080")))
        lst.add(Product(Name = "Soname", Price = "500",Images = mutableListOf("https://images.unsplash.com/photo-1532581291347-9c39cf10a73c?w=1080")))
        lst.add(Product(Name = "Soname", Price = "500",Images = mutableListOf("https://images.unsplash.com/photo-1532581291347-9c39cf10a73c?w=1080")))
        lst.add(Product(Name = "Soname", Price = "500",Images = mutableListOf("https://images.unsplash.com/photo-1534447677768-be436bb09401?w=1080")))
        lst.add(Product(Name = "Soname", Price = "500",Images = mutableListOf("https://images.unsplash.com/photo-1532581291347-9c39cf10a73c?w=1080")))
        lst.add(Product(Name = "Soname", Price = "500",Images = mutableListOf("https://images.unsplash.com/photo-1532581291347-9c39cf10a73c?w=1080")))
        lst.add(Product(Name = "Soname", Price = "500",Images = mutableListOf("https://images.unsplash.com/photo-1534447677768-be436bb09401?w=1080")))
        lst.add(Product(Name = "Soname", Price = "500",Images = mutableListOf("https://images.unsplash.com/photo-1532581291347-9c39cf10a73c?w=1080")))

        rv.layoutManager =GridLayoutManager(requireContext(),3,GridLayoutManager.VERTICAL,false)
        var adapter = GroupAdapter<GroupieViewHolder>()
        for(data in lst)
        {
            adapter.add(ProductShowAdapter(requireContext(),data))
        }
        carousel.registerLifecycle(lifecycle)
        list.add(
            CarouselItem(
                imageUrl = "https://images.unsplash.com/photo-1532581291347-9c39cf10a73c?w=1080",
                caption = "Photo by Aaron Wu on Unsplash"
            )
        )
        val headers = mutableMapOf<String, String>()
        headers["header_key"] = "header_value"

        list.add(
            CarouselItem(
                imageUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=1080",
                headers = headers
            )
        )


        list.add(
            CarouselItem(
                imageUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=1080"
            )
        )
        carousel.setData(list)
        rv.adapter = adapter


        return view
    }

}