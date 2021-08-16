package com.xrest.buysell.Fragments

import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.xrest.buysell.R
import java.util.*

class MapsFragment(val location:LatLng) : Fragment() {

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
        googleMap.uiSettings.isZoomControlsEnabled = true
        val sydney =location
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.Builder().zoom(14f).target(location).build()))
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Location").snippet(getAdderess(location)))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
    private fun getAdderess(latLng: LatLng): String {
        val geo = Geocoder(requireContext(), Locale.getDefault())
        val arrayAddress = geo.getFromLocation(latLng.latitude, latLng.longitude, 1)
        return  arrayAddress[0].getAddressLine(0).toString()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view= inflater.inflate(R.layout.fragment_maps, container, false)
        view.findViewById<Button>(R.id.nav).setOnClickListener(){
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=${location.latitude},${location.longitude}&mode=l"))
            intent.setPackage("com.google.android.apps.maps")
            if(intent.resolveActivity(requireActivity().packageManager)!=null)
            {
               requireContext().startActivity(intent)
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}