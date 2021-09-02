package com.xrest.buysell.Fragments

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.xrest.buysell.R
import com.xrest.buysell.Retrofit.Product
import java.util.*

class UpdateProduct(val product: Product) : Fragment() {


    private val category = arrayOf("Electronics", "AutoMobiles", "Furniture", "Real State")
    private lateinit var acategory:String
    private lateinit var acondition:String
    private lateinit var asub:String
    private  var nogotiable:Boolean?=null
    private   val condition = arrayOf("Brand New", "Like New", "Used")
    private val subOne =arrayOf("Mobile", "Computer & Peripherals", "Gadgets")
    private  var mc:MutableMap<TextView, EditText> = mutableMapOf()
    private  val subTwo=arrayOf("Bike", "Car", "Scooter")
    private val subThree=arrayOf("Sofa", "Bed", "Table", "Chair", "CupBoard")
    private val subFour=arrayOf("House", "Land", "Office")
    private   lateinit var conditions: Spinner
    private    lateinit var categorys: Spinner
    private lateinit var sub: Spinner
    private lateinit var add: ImageView
    private lateinit var usedMain: LinearLayout
    private lateinit var containers: LinearLayout
    private lateinit var register: TextView
    private lateinit var googleMap: GoogleMap
    private lateinit var frameLayout: FrameLayout
    private lateinit var search: FloatingActionButton
    private lateinit var imageView: ImageView
    private lateinit var name: EditText
    private lateinit var price: EditText
    private lateinit var used: EditText
    private lateinit var description: EditText
    private lateinit var yes: RadioButton
    private lateinit var no: RadioButton
    private var images:MutableList<String> = mutableListOf()
    var latitude =""
    var longitude =""
    var index  = 0
    val cb: OnMapReadyCallback
        @SuppressWarnings("MissingPermission")
        get()= OnMapReadyCallback { p0 ->
            googleMap =p0
            googleMap.isMyLocationEnabled = true
            googleMap.uiSettings.isMyLocationButtonEnabled = true
            googleMap.uiSettings.isZoomControlsEnabled =true
            googleMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
                override fun onMarkerDragStart(p0: Marker) {
                }

                override fun onMarkerDrag(p0: Marker) {
                }

                override fun onMarkerDragEnd(p0: Marker) {
                    googleMap.clear()
                    googleMap.addMarker(
                        MarkerOptions().draggable(true).title("Drag The Marker to The CITE")
                            .snippet(
                                getAdderess(
                                    LatLng(
                                        p0.position.latitude,
                                        p0.position.longitude
                                    )
                                )
                            ).draggable(true)
                            .position(LatLng(p0.position.latitude, p0.position.longitude))
                    )
                    latitude = p0.position.latitude.toString()
                    longitude =p0.position.longitude.toString()
                    Log.d("location","${p0}")

                }
            })
            googleMap.setOnMapClickListener { p0 ->
                googleMap.clear()
                googleMap.addMarker(
                    MarkerOptions().draggable(true).title("Drag The Marker to The CITE")
                        .snippet(
                            getAdderess(
                                p0
                            )
                        ).draggable(true).position(p0)
                )
                latitude = p0.latitude.toString()
                longitude =p0.longitude.toString()
                Log.d("location","${p0}")
            }
        }

    private fun getAdderess(latLng: LatLng): String {
        val geo = Geocoder(requireContext(), Locale.getDefault())
        val arrayAddress = geo.getFromLocation(latLng.latitude, latLng.longitude, 1)
        return  arrayAddress[0].getAddressLine(0).toString()

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update_product, container, false)
        if(!Places.isInitialized())
        {
            Places.initialize(requireContext(), getString(R.string.google_maps_key))
        }
        add = view.findViewById(R.id.add)
        search = view.findViewById(R.id.search)
        frameLayout = view.findViewById(R.id.map)
        register = view.findViewById(R.id.register)
        name = view.findViewById(R.id.name)
        used = view.findViewById(R.id.used)
        description = view.findViewById(R.id.description)
        price = view.findViewById(R.id.price)
        yes = view.findViewById(R.id.yes)
        no = view.findViewById(R.id.no)
        usedMain = view.findViewById(R.id.usedMain)
        containers = view.findViewById(R.id.containers)
        usedMain.isVisible=false
        conditions = view.findViewById(R.id.condition)
        categorys = view.findViewById(R.id.category)
        sub = view.findViewById(R.id.sub)
name.setText(product.Name)
        used.setText(product.UsedFor.toString())
        description.setText(product.Description)
        price.setText(product.Price.toString())
        if(product.Negotiable == true)
        {
            yes.isChecked = true
        }
        else{
            no.isChecked = true
        }
        if(product.Condition.equals("Brand New"))
        {
            usedMain.isVisible = false
        }

        frameLayout.isVisible = product.Category.equals("Real State")

        return view
    }
}