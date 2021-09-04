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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
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
    var ind =""
    var latitude =""
    var longitude =""
    var index  = 0
     var currentIndex = 0
    val cb: OnMapReadyCallback
        @SuppressWarnings("MissingPermission")
        get()= OnMapReadyCallback { p0 ->
            googleMap =p0
            googleMap.addMarker(
                MarkerOptions().draggable(true).title("Drag The Marker to The CITE")
                    .snippet(
                        getAdderess(
                            LatLng(
                                product.Features!![3]?.feature?.toDouble()!!,
                                product.Features!![4]?.feature?.toDouble()!!
                            )
                        )
                    ).draggable(true)
                    .position(LatLng( product.Features!![3]?.feature?.toDouble()!!,
                        product.Features!![4]?.feature?.toDouble()!!))
            )
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.Builder().target(
                LatLng( product.Features!![3]?.feature?.toDouble()!!,
                    product.Features!![4]?.feature?.toDouble()!!)
            ).zoom(14f).build()))
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
        sub.isVisible = false;
        name.setText(product.Name)
        used.setText(product.UsedFor.toString())
        description.setText(product.Description)
        price.setText(product.Price.toString())
        nogotiable = product.Negotiable
        acategory = product.Category!!
        acondition = product.Condition!!
        asub = product.SubCategory!!
        add.setOnClickListener(){
            addDynamicImageView(view)
        }
        if(product.Negotiable == true)
        {
            yes.isChecked = true

        }
        else{
            no.isChecked = true
        }
        sub.isVisible = false
search.setOnClickListener(){
    var lst: MutableList<Place.Field> = mutableListOf()
    lst.add(Place.Field.LAT_LNG)
    lst.add(Place.Field.ADDRESS)
    lst.add(Place.Field.NAME)
    var intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, lst)
        .build(requireContext())
    startActivityForResult(intent, 200)
}
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

//        register.setOnClickListener(this)
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






        conditions.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var condition = parent!!.getItemAtPosition(position)
                usedMain.isVisible = !condition.equals("Brand New")

                acondition = parent.getItemAtPosition(position).toString()

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }


        if(product.Condition.equals("Brand New"))
        {
            usedMain.isVisible = false
        }

       if(product.Category.equals("Real State")){
           latitude = product!!.Features?.get(3)!!.feature!!
           longitude = product!!.Features?.get(4)!!.feature!!
           val map = SupportMapFragment.newInstance() as SupportMapFragment
           map.getMapAsync(cb)
           requireFragmentManager().beginTransaction().replace(R.id.map, map).commit()
           frameLayout.isVisible = true
       }
        for(i in 0..category.size-1)
        {
            if(category[i].equals(product.Category))
            {
                categorys.setSelection(i)
            }
        }
        for(data in 0..condition.size -1)
        {
            if(condition[data].equals(product.Condition))
            {
                conditions.setSelection(data)
            }
        }
        if(product.Images?.size!!>0)
        {
            for(i in 0..product.Images?.size!! - 1)
            {
                addDynamicImageView(product.Images!![i],i,view)
            }
        }
        addDynamicEditText(addViews());
        register.setOnClickListener(){
            if (yes.isChecked == true) {
                nogotiable = true
            } else if (no.isChecked) {
                nogotiable = false
            }
            var feature: MutableList<Features> = mutableListOf()
            if (acategory != "Furniture") {
                for (data in mc) {
                    Log.d("Data", data.key.text.toString())
                    Log.d("valuie", data.value.text.toString())
                    feature.add(
                        Features(
                            name = data.key.text!!.toString(),
                            feature = data.value.text.toString()
                        )
                    )
                }
            }
            if (acategory == "Real State") {
            feature[3].feature= latitude.toString()
                feature[4].feature = longitude.toString()
            }
            var product = Product(
                Name = name.text.toString(),
                UsedFor = used.text.toString().toInt(),
                Price = price.text.toString(),
                Description = description.text.toString(),
                Negotiable = nogotiable,
                Category = acategory,
                Condition = acondition,
                SoldOut = false,
                Features = feature,
                SubCategory = asub
            )
            var edtLst = mutableListOf<EditText>(name, price, description, used)
            if (index > 0) {
                if (validate(edtLst)) {
                    post(product)

                }

            } else {
                Toast.makeText(requireContext(), "Please Add Image First", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        return view


    }
    private fun post(productd: Product) {
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

                var response = ProductRepo().update(product._id!!,productd)
                if(response.success ==true){
                    withContext(Dispatchers.Main)
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

    fun validate(edt:MutableList<EditText>):Boolean{
        var flag = true
        for(data in edt)
        {
            if(data.text.toString().length ==0)
            {
                when(data.id)
                {

                    R.id.name -> data.setError("Please Enter Product Name")
                    R.id.used -> data.setError("Please Product Used In Years")
                    R.id.description -> data.setError("Please Enter Product Description")
                    R.id.price -> data.setError("Please Enter Your Price")

                }
                flag = false
            }
        }
        if(name.text.toString().contains("1") || name.text.toString().contains("2") || name.text.toString().contains("3") ||name.text.toString().contains("4") ||name.text.toString().contains("5") ||name.text.toString().contains("6") ||name.text.toString().contains("7") ||name.text.toString().contains("8") ||name.text.toString().contains("9") ||name.text.toString().contains("0"))
        {
            name.setError("No Number Allowed in Name")
            flag = false
        }
        return flag
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
            var response = ProductRepo().uploadProduct(product._id!!,ind,array)

            if(response.success==true)
            {
                withContext(Dispatchers.Main){
                    lottie.setAnimation(R.raw.success)
                    lottie.loop(true)
                    lottie.playAnimation()
                    success.text = "Complete"
                    price.setText(null)
                    name.setText(null)
                    description.setText(null)
                    used.setText(null)
                    dialog.cancel()
                    (requireActivity() as AppCompatActivity).supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fl,Home())
                        commit()
                        addToBackStack(null)
                    }
                }
            }

        }

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






    fun addDynamicImageView(view:View){
        add.isEnabled = false
        if(product.Images!!.size==4)
        {
            add.isVisible = false
        }
        var container :LinearLayout = view.findViewById(R.id.container)
        imageView = ImageView(requireContext())
        var params = LinearLayout.LayoutParams(250, 250)
        params.setMargins(6, 6, 6, 6)
        imageView.scaleType = ImageView.ScaleType.FIT_XY
        imageView.layoutParams = params
        imageView.setOnClickListener(){
            currentIndex = product.Images!!.size!!
            showPopup(imageView)
        }
        container.addView(imageView)
        product.Images!!.add("")
    }
    fun addDynamicImageView(data:String,position:Int,view: View){
        add.isEnabled = false
        if(product.Images!!.size==4)
        {
            add.isVisible = false
        }
        var container :LinearLayout = view?.findViewById(R.id.container)
        imageView = ImageView(requireContext())
        var params = LinearLayout.LayoutParams(250, 250)
        params.setMargins(6, 6, 6, 6)
        imageView.scaleType = ImageView.ScaleType.FIT_XY
        imageView.layoutParams = params
        Glide.with(requireContext()).load(RetroftiService.loadImage(data)).into(imageView)
        imageView.setOnClickListener(){
            showPopup(imageView)
            currentIndex = position
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






    fun addViews():MutableMap<TextView, EditText>{
        var lst:MutableMap<TextView, EditText> = mutableMapOf()
        for( data in product!!.Features!!)
        {
            val txt = TextView(requireContext())
            txt.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            txt.text = data.name.toString()

            var layoutParams = LinearLayout.LayoutParams(500, 150)
            var edt:EditText = EditText(requireContext())
            edt.hint ="${data}"
            layoutParams.setMargins(30, 30, 5, 5)
            edt.layoutParams = layoutParams
            edt.setPadding(30, 30, 30, 30)
            edt.setBackgroundColor(Color.WHITE)
            edt.setText(data.feature.toString())
            lst[txt] = edt

        }

        return lst
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && data!=null)
        {
            if(requestCode == 200 && data!=null)
            {

                var data = Autocomplete.getPlaceFromIntent(data)
                var latlng = data.latLng
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
                latitude = latlng!!.latitude.toString()!!
                longitude = latlng.longitude.toString()
            }
            else if(requestCode == 0 && data!= null)
            {
                index++
                imageView.setImageBitmap(
                    BitmapFactory.decodeFile(
                        RetroftiService.getDataFromGallery(
                            requireContext(),
                            data
                        )!!
                    )
                )

             if(!ind.contains(currentIndex.toString()))
             {
                 ind+=currentIndex.toString()
             }

                    images.add(RetroftiService.getDataFromGallery(requireContext(), data)!!)



                Log.d("afterText", images.toString())
                add.isEnabled = true
                imageView.setOnClickListener(){}

            }
            else if(requestCode ==1 && data !=null)
            {
                index++

                imageView.setImageBitmap(
                    BitmapFactory.decodeFile(
                        RetroftiService.getDataFromCamera(
                            requireContext(),
                            data!!
                        )!!
                    )
                )
                if(!ind.contains(currentIndex.toString()))
                {
                    ind+=currentIndex.toString()
                }

                images.add(RetroftiService.getDataFromCamera(requireContext(), data)!!)

                Log.d("afterText", images.toString())
                add.isEnabled = true
                imageView.setOnClickListener(){}


            }
        }
    }

}