package com.xrest.buysell.Fragments

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager

import android.graphics.BitmapFactory
import android.graphics.Color

import android.location.Geocoder
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.xrest.buysell.R
import com.xrest.buysell.Retrofit.Features
import com.xrest.buysell.Retrofit.Product
import com.xrest.buysell.Retrofit.Repo.ProductRepo
import com.xrest.buysell.Retrofit.RetroftiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*

class AddPost : Fragment(), View.OnClickListener {

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
    private   lateinit var conditions:Spinner
    private    lateinit var categorys:Spinner
    private lateinit var sub:Spinner
    private lateinit var add:ImageView
    private lateinit var usedMain:LinearLayout
    private lateinit var containers:LinearLayout
    private lateinit var register:TextView
    private lateinit var googleMap:GoogleMap
    private lateinit var frameLayout: FrameLayout
    private lateinit var search:FloatingActionButton
    private lateinit var imageView:ImageView
    private lateinit var name:EditText
    private lateinit var price:EditText
    private lateinit var used:EditText
    private lateinit var description:EditText
    private lateinit var yes:RadioButton
    private lateinit var no:RadioButton
    private var images:MutableList<String> = mutableListOf()
    val cb:OnMapReadyCallback
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

        if(!Places.isInitialized())
        {
            Places.initialize(requireContext(), getString(R.string.google_maps_key))
        }
        val view = inflater.inflate(R.layout.fragment_add_post, container, false)
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

        frameLayout.isVisible = false
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 1
            )
        }

        register.setOnClickListener(this)
        conditions.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            condition
        )
        categorys.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            category
        )

        categorys.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                var adapter:ArrayAdapter<String>?=null
               acategory = parent!!.getItemAtPosition(position).toString()
                when(parent.getItemAtPosition(position).toString())
                {
                    "Electronics" -> {
                        adapter = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_list_item_1,
                            subOne
                        )
                    }
                    "Furniture" -> {
                        adapter = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_list_item_1,
                            subThree
                        )

                    }
                    "Real State" -> {
                        adapter = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_list_item_1,
                            subFour
                        )

                    }
                    "AutoMobiles" -> {
                        adapter = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_list_item_1,
                            subTwo
                        )

                    }
                }
                sub.adapter =adapter

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }



        search.setOnClickListener(this)
        sub.onItemSelectedListener = object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
               val type = parent!!.getItemAtPosition(position).toString()
                asub = type
               if(type.equals("Computer & Peripherals") || type.equals("Mobile"))
               {
                   frameLayout.isVisible = false

                   val lstx:MutableMap<String, String> = mutableMapOf()
                   lstx["RAM"] = "Ram"
                   lstx["ROM"] = "ROM"
                   lstx["Processor"] = "Processor"
                   lstx["Display"] = "Display"
                 addDynamicEditText(addViews(lstx))
               }
                else if(type.equals("Car")||type.equals("Bike")|| type.equals("Scooter"))
               {
                   frameLayout.isVisible = false

                   val lstx:MutableMap<String, String> = mutableMapOf()
                   lstx["Company"] = "Company"
                   lstx["Lot_No"] = "-xxx"
                   lstx["Model_Year"] = "YYYY"
                   lstx["Color"] = "Color"
                   addDynamicEditText(addViews(lstx))
                }
                else if(type.equals("House") || type.equals("Land")||type.equals("Office"))
               {
                   frameLayout.isVisible = true
                   val lstx:MutableMap<String, String> = mutableMapOf()
                   lstx["AANA"] = "Land in AANA"
                   lstx["SQUARE FEET"] = "SQFT"
                   lstx["ADDRESS"] = "ADDRESS"
                   val map = SupportMapFragment.newInstance() as SupportMapFragment
                   map.getMapAsync(cb)
                   requireFragmentManager().beginTransaction().replace(R.id.map, map).commit()
                   addDynamicEditText(addViews(lstx))

                }
                else{
                    containers!!.isVisible= false
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }



        }


conditions.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var condition = parent!!.getItemAtPosition(position)
        usedMain.isVisible = !condition.equals("Brand New")

        acondition = parent.getItemAtPosition(position).toString()

    }
    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}
        var count = 0
        add.setOnClickListener(){
            count +=1
            addDynamicImageView(count)
        }

        return view
    }

    private fun addDynamicEditText(electronics: MutableMap<TextView, EditText>) {
        containers.removeAllViews()
        containers!!.isVisible= true

        mc.clear()
        mc= electronics
        for(data in mc)
        {
            var linearLayout = LinearLayout(requireContext())
            linearLayout.orientation = LinearLayout.HORIZONTAL
            var params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 60, 0, 0)
            linearLayout.layoutParams = params
            linearLayout.addView(data.key)
            linearLayout.addView(data.value)
            linearLayout.setBackgroundResource(R.drawable.edt)
            containers.addView(linearLayout)
        }

    }






    fun addDynamicImageView(count: Int){
        add.isEnabled = false
    if(count==4)
    {
        add.isVisible = false
    }
    var container :LinearLayout = requireView()?.findViewById(R.id.container)
     imageView = ImageView(requireContext())
    var params = LinearLayout.LayoutParams(250, 250)
    params.setMargins(6, 6, 6, 6)
    imageView.scaleType = ImageView.ScaleType.FIT_XY
    imageView.layoutParams = params
    imageView.setImageResource(R.drawable.background_image_one_signin)

        imageView.setOnClickListener(){

            showPopup(imageView)
        }
    container.addView(imageView)
}

    private fun showPopup(profile: ImageView) {
        val popUp = PopupMenu(requireContext(), profile)
        popUp.menuInflater.inflate(R.menu.camera_gallery, popUp.menu)
        try{
            val popup = PopupMenu::class.java.getDeclaredField("mPopup")
            popup.isAccessible = true
            val menu = popup.get(popUp)
            menu.javaClass
                .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                .invoke(menu, true)
        }
        catch (ex: Exception)
        { ex.printStackTrace()
        } finally {
            popUp.show()
            popUp.setOnMenuItemClickListener {
                when(it.itemId)
                {
                    R.id.camera -> {

                        var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(intent, 1)

                    }
                    R.id.gallery -> {
                        var intent = Intent(Intent.ACTION_PICK)
                        intent.type = "image/*"
                        startActivityForResult(intent, 0)
                    }
                }
                true
            }

        }


    }






fun addViews(lstx: MutableMap<String, String>):MutableMap<TextView, EditText>{
    var lst:MutableMap<TextView, EditText> = mutableMapOf()
    for( data in lstx.keys)
    {
        val txt = TextView(requireContext())
        txt.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        txt.text = data.toString()

        var layoutParams = LinearLayout.LayoutParams(500, 150)
        var edt:EditText = EditText(requireContext())
edt.hint ="${data}"
        layoutParams.setMargins(30, 30, 5, 5)
        edt.layoutParams = layoutParams
        edt.setPadding(30, 30, 30, 30)
        edt.setBackgroundColor(Color.WHITE)

        lst[txt] = edt

    }

    return lst
}






    override fun onClick(v: View?) {
        when(v?.id)
        {
            R.id.register -> {
                var feature:MutableList<Features> = mutableListOf()
                for (data in mc)
                {
                    Log.d("Data",data.key.text.toString())
                    Log.d("valuie",data.value.text.toString())
                    feature.add(Features(name=data.key.text!!.toString(),feature = data.value.text.toString()))
                }
                var product = Product(Name= name.text.toString(),UsedFor = used.text.toString().toInt(),Price = price.text.toString(),Description = description.text.toString(),Negotiable = nogotiable,Category = acategory, Condition = acondition,SoldOut = false,Features = feature)
                post(product)
            }
            R.id.search -> {
                var lst: MutableList<Place.Field> = mutableListOf()
                lst.add(Place.Field.LAT_LNG)
                lst.add(Place.Field.ADDRESS)
                lst.add(Place.Field.NAME)
                var intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, lst)
                    .build(requireContext())
                startActivityForResult(intent, 200)
            }
        }
    }

    private fun post(product: Product) {
        if(yes.isChecked)
        {
            nogotiable= true
        }
        else if(no.isChecked)
        {
            nogotiable= false
        }

       Log.d("alterText",nogotiable.toString())
        CoroutineScope(Dispatchers.IO).launch {
            try {

                var response = ProductRepo().post(product)
                if(response.success ==true){
                    withContext(Main)
                    {
                        uplloadImage(response.message!!)

                    }
                }
                else{

                    withContext(Dispatchers.Main){
                        Toast.makeText(requireContext(), "OOPS!! Something Went Wrong", Toast.LENGTH_SHORT).show()
                    }
                }

            }
            catch (ex:java.lang.Exception){
                ex.printStackTrace()
            }

        }



    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && data!=null)
        {
            if(requestCode == 200 && data!=null)
            {

                var data = Autocomplete.getPlaceFromIntent(data)
                googleMap.clear()
                googleMap.addMarker(
                    MarkerOptions().position(data.latLng).title("Your Searched Destination")
                        .draggable(
                            true
                        )
                )
                googleMap.animateCamera(
                    CameraUpdateFactory.newCameraPosition(
                        CameraPosition.Builder().target(
                            data.latLng
                        ).zoom(14f).build()
                    )
                )

            }
            else if(requestCode == 0 && data!= null)
            {
                images.add(RetroftiService.getDataFromGallery(requireContext(), data)!!)
                imageView.setImageBitmap(
                    BitmapFactory.decodeFile(
                        RetroftiService.getDataFromGallery(
                            requireContext(),
                            data
                        )!!
                    )
                )
                Log.d("afterText", images.toString())
                add.isEnabled = true
                imageView.setOnClickListener(){}

            }
            else if(requestCode ==1 && data !=null)
            {
                images.add(RetroftiService.getDataFromCamera(requireContext(), data!!)!!)
                imageView.setImageBitmap(
                    BitmapFactory.decodeFile(
                        RetroftiService.getDataFromCamera(
                            requireContext(),
                            data!!
                        )!!
                    )
                )
                Log.d("afterText", images.toString())
                add.isEnabled = true
                imageView.setOnClickListener(){}


            }
        }
    }


    fun uplloadImage(id:String){

        var array :MutableList<MultipartBody.Part> = mutableListOf()
        for(data in images)
        {
            var file = File(data)
            var extention = MimeTypeMap.getFileExtensionFromUrl(data)
            var mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extention)
            var requestBody = RequestBody.create(MediaType.parse(mimeType),file)
            var body = MultipartBody.Part.createFormData("image",file.name,requestBody)
            array.add(body)

        }
        var dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.success_notification)
        dialog.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        var lottie = dialog.findViewById<LottieAnimationView>(R.id.lottie)
        var success = dialog.findViewById<TextView>(R.id.success)
        success.text="Uploading"
        dialog.show()
        dialog.setCancelable(false)

    CoroutineScope(Dispatchers.IO).launch {
var response = ProductRepo().uploadImage(id,array)

        if(response.success==true)
        {
withContext(Main){
    lottie.setAnimation(R.raw.success)
    lottie.loop(true)
    lottie.playAnimation()
    success.text = "Complete"
    dialog.cancel()
    requireFragmentManager().beginTransaction().apply {
        replace(R.id.fl,AddPost())
        commit()
    }
}
        }

    }

    }

    override fun onResume() {
        (requireActivity() as AppCompatActivity).supportActionBar?.title="Add Post"
        super.onResume()
    }



}