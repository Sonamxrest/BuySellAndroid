package com.xrest.buysell.Fragments

import android.os.Bundle
import android.util.Log
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
import com.xrest.buysell.Retrofit.Repo.ProductRepo
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.imaginativeworld.whynotimagecarousel.ImageCarousel
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import java.util.*


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
CoroutineScope(Dispatchers.IO).launch{
    try {
        var response = ProductRepo().getPost()
        if(response.success == true)
        {
            withContext(Main){
                lst = response.data!!
                lst.shuffle()
                rv.layoutManager =GridLayoutManager(requireContext(),3,GridLayoutManager.VERTICAL,false)
                var adapter = GroupAdapter<GroupieViewHolder>()
                Log.d("data in list", lst.toString())
                for(data in lst)
                {
                    if(data.SoldOut!! == false)
                    {
                        adapter.add(ProductShowAdapter(requireContext(),data))
                    }
                }
                rv.adapter = adapter
            }

        }
    }
    catch (ex:Exception){

    }
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
        carousel.autoPlay = true
        return view
    }
    override fun onResume() {

        var actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar!!.show()
        super.onResume()
    }
}