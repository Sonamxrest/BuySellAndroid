package com.xrest.buysell.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.xrest.buysell.R
import com.xrest.buysell.Retrofit.Product

class ProductDescription(val product:Product) : Fragment(), OnMapReadyCallback {
    private lateinit var name:TextView
    private lateinit var price:TextView
    private lateinit var description:TextView
    private lateinit var negotiable:TextView
    private lateinit var condition:TextView
    private lateinit var category:TextView
    private lateinit var type:TextView
    private lateinit var usedFor:TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =inflater.inflate(R.layout.fragment_product_description, container, false)
        name = view.findViewById(R.id.name)
        price = view.findViewById(R.id.price)
        description = view.findViewById(R.id.description)
        negotiable= view.findViewById(R.id.negotiable)
        condition = view.findViewById(R.id.condition)
        type =view.findViewById(R.id.type)
        category = view.findViewById(R.id.category)
        usedFor = view.findViewById(R.id.used)

name.text = this.product.Name
        price.text = this.product.Price
            description.text = this.product.Description
                category.text = this.product.Category
                    type.text = this.product.SubCategory
                        usedFor.text = this.product.UsedFor.toString()
                            condition.text = this.product.Condition
                               negotiable.text ="NO"
                                if(this.product.Negotiable!!)
                                {
                                    negotiable.text ="YES"

                                }



        return view
    }

    override fun onMapReady(p0: GoogleMap) {

    }


}