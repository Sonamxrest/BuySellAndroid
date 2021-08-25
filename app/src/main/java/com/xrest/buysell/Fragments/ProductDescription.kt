package com.xrest.buysell.Fragments

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.xrest.buysell.R
import com.xrest.buysell.Retrofit.Product
import java.util.*

class ProductDescription(val product:Product) : Fragment() {
    private lateinit var name:TextView
    private lateinit var price:TextView
    private lateinit var description:TextView
    private lateinit var negotiable:TextView
    private lateinit var condition:TextView
    private lateinit var category:TextView
    private lateinit var type:TextView
    private lateinit var usedFor:TextView
    private lateinit var features:TextView
    private lateinit var googleMap:GoogleMap
    private lateinit var map:FrameLayout
val callback:OnMapReadyCallback
    @SuppressWarnings("MissingPermission")
    get()= OnMapReadyCallback { p0 ->
        googleMap = p0
        googleMap.isMyLocationEnabled = true
        googleMap.uiSettings.isMyLocationButtonEnabled = true
        googleMap.uiSettings.isZoomControlsEnabled = true
        var location = LatLng(product.Features?.get(3)?.feature!!.toDouble(),product.Features?.get(4)?.feature!!.toDouble())
        Log.d("location","$location")
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.Builder().zoom(14f).target(location).build()))
        googleMap.addMarker(MarkerOptions().position(location).snippet(getAdderess(location)))

    }
//    private val callback = OnMapReadyCallback { googleMap ->
//        if(product.Category=="Real State")
//        {
//            val sydney = LatLng(product.Features?.get(3)?.feature!!.toDouble(), product.Features?.get(4)?.feature!!.toDouble())
//            googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        }
//    }

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
        features =view.findViewById(R.id.feature)
        map = view.findViewById(R.id.map)
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
        var feature =""
        if(product.Category!="Real State")
        {
            for(data in this.product.Features!!)
            {
                feature +="\n ${data.name}: ${data.feature}"

            }
            features.text = feature
            map.isVisible = false
        }
        else{
            map.isVisible = true
            view.findViewById<FloatingActionButton>(R.id.m).setOnClickListener(){

                var location = LatLng(product.Features?.get(3)?.feature!!.toDouble(),product.Features?.get(4)?.feature!!.toDouble())
                var fragment = MapsFragment(location, product.Images?.get(0)!!)
                var activity = requireContext() as AppCompatActivity
                activity.supportFragmentManager.beginTransaction().replace(R.id.fl,fragment).addToBackStack(null).commit()
            }
            val map = SupportMapFragment.newInstance() as SupportMapFragment
            map.getMapAsync(callback)
            requireFragmentManager().beginTransaction().replace(R.id.map, map).commit()
        }



        return view
    }

    private fun getAdderess(latLng: LatLng): String {
        val geo = Geocoder(requireContext(), Locale.getDefault())
        val arrayAddress = geo.getFromLocation(latLng.latitude, latLng.longitude, 1)
        return  arrayAddress[0].getAddressLine(0).toString()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }


}