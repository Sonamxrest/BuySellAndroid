package com.xrest.buysell.Fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.media.Image
import android.net.Uri
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.directions.route.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.xrest.buysell.R
import com.xrest.buysell.Retrofit.RetroftiService
import java.util.*

class MapsFragment(val location:LatLng, val image:String) : Fragment(), RoutingListener, GoogleMap.OnMyLocationChangeListener {
    var polylines:MutableList<Polyline> = mutableListOf()
    var origin = LatLng(0.0,0.0)
    lateinit var googleMap: GoogleMap
    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
          this.googleMap = googleMap
        googleMap.uiSettings.isZoomControlsEnabled = true
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
           ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION),0)
        }
        googleMap.isMyLocationEnabled = true
        googleMap.setOnMyLocationChangeListener(){
            googleMap.animateCamera(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition.Builder().target(
                        LatLng(
                            location.latitude,
                            location.longitude
                        )
                    ).zoom(14f).build()
                )
            )
            origin = LatLng(it.latitude,it.longitude)
            findRoutes(origin,location)
        }

        val sydney =location
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.Builder().zoom(14f).target(location).build()))
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Location").snippet(getAdderess(location)))

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view= inflater.inflate(R.layout.fragment_maps, container, false)

        var dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(R.layout.bottomsheet)
        dialog.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        var array:FloatArray =FloatArray(10)
        Location.distanceBetween(origin.latitude, origin.longitude, location.latitude, location.longitude, array)
        var place: TextView = dialog.findViewById<TextView>(R.id.place)!!
        var distance: TextView = dialog.findViewById<TextView>(R.id.distance)!!
        var images: ImageView = dialog.findViewById<ImageView>(R.id.image)!!
        Glide.with(requireContext()).load(RetroftiService.loadImage(image!!)).into(images)
        place.text = getAdderess(location)

        var requestBtn : Button = dialog.findViewById<Button>(R.id.idRequest)!!
        requestBtn.setOnClickListener(){
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=${location.latitude},${location.longitude}&mode=l"))
            intent.setPackage("com.google.android.apps.maps")
            if(intent.resolveActivity(requireActivity().packageManager)!=null)
            {
                requireContext().startActivity(intent)
            }

        }
        var km=(array[0]/1000)
        distance.text =km.toString() +
                "Meters From Your Location"
            dialog.show()
        dialog.setCancelable(false)
        return view
    }
    private fun getAdderess(latLng: LatLng): String {
        val geo = Geocoder(requireContext(), Locale.getDefault())
        val arrayAddress = geo.getFromLocation(latLng.latitude, latLng.longitude, 1)
        return  arrayAddress[0].getAddressLine(0).toString()

    }

    private fun findRoutes(origin: LatLng, destination: LatLng) {


        if(origin ==null || destination ==null)
        {
            Toast.makeText(context, "Unable to fetch location", Toast.LENGTH_SHORT).show()
        }
        else{
            var routes = Routing.Builder().
            travelMode(AbstractRouting.TravelMode.DRIVING)
                .key(getString(R.string.google_maps_key))
                .withListener(this)
                .waypoints(origin, destination)
                .build()
            routes.execute()
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onRoutingFailure(p0: RouteException?) {

    }

    override fun onRoutingStart() {

    }

    override fun onRoutingSuccess(p0: ArrayList<Route>?, p1: Int) {
        if (polylines != null) {
            polylines!!.clear()
        }
        var polyOptions = PolylineOptions()
        var polylineStartLatLng: LatLng? = null
        var polylineEndLatLng: LatLng? = null
        polylines = mutableListOf()

        for( i in 0..p0!!.size -1 ) {
            if (i == p1)
            {
                polyOptions.color(resources.getColor(R.color.black))
                polyOptions.width(7f)
                polyOptions.addAll(p0.get(p1).points)
                var pLine = googleMap.addPolyline((polyOptions))
                polylineStartLatLng =pLine.points.get(0)
                var k:Int = pLine.points.size
                polylineEndLatLng = pLine.points.get(k - 1)
                polylines!!.add(pLine)

            }
            else{


            }


        }
        val startMarker = MarkerOptions()
        startMarker.position(polylineStartLatLng)
        startMarker.title("My Location")
        googleMap.addMarker(startMarker)


        val endMarker = MarkerOptions()
        endMarker.position(polylineEndLatLng)
        endMarker.title("Destination")
        googleMap.addMarker(endMarker)
    }

    override fun onRoutingCancelled() {

    }

    override fun onMyLocationChange(it: Location) {
        origin = LatLng(it.latitude, it.longitude)

    }
}