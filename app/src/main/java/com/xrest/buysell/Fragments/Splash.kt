package com.xrest.buysell.Fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.gms.maps.model.Dash
import com.xrest.buysell.Activity.Dashboard
import com.xrest.buysell.R
import com.xrest.buysell.Retrofit.Repo.UserRepository
import com.xrest.buysell.Retrofit.RetroftiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class Splash : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

var view = inflater.inflate(R.layout.fragment_splash, container, false)
      RetroftiService.isOnline = checkNetworkConnection()
        if(RetroftiService.isOnline!!)
        {

            Handler().postDelayed({
                if(requireContext().getSharedPreferences("onBoarding",Activity.MODE_PRIVATE).getBoolean("Boarding",false)!=true)
                {
                    Navigation.findNavController(requireView()).navigate(R.id.action_splash_to_startActions)

                }
                else{
                    var pref = requireContext().getSharedPreferences("userLogin",Activity.MODE_PRIVATE)
                    CoroutineScope(Dispatchers.IO).launch {

                        var response =
                            UserRepository().login(pref.getString("username","")!!,pref.getString("password","")!!)
                        if (response.success == true) {
                            withContext(Dispatchers.Main)
                            {

                                RetroftiService.token ="Bearer "+ response.token!!
                                RetroftiService.users = response.user!!
                                requireContext().startActivity(Intent(requireContext(), Dashboard::class.java))

                            }
                        } else {
                            withContext(Main)
                            {
                                Navigation.findNavController(requireView()).navigate(R.id.action_splash_to_loginSignup)

                            }
                        }


                    }

                } },3000)

        }
        else{
            Toast.makeText(requireContext(), "Unregistered Network", Toast.LENGTH_SHORT).show()
        }
        return view

    }


    fun checkNetworkConnection():Boolean{
        var result:Boolean= true
        var connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            var network = connectivityManager.activeNetwork ?:return false
            var networkCpabalities = connectivityManager.getNetworkCapabilities(network) ?:return false
            result = when{
                networkCpabalities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)->true
                networkCpabalities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)->true
                else-> false
            }
        }
        else{
            connectivityManager.run {

                connectivityManager.activeNetworkInfo?.run {
                    result = when(type)
                    {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        else->false
                    }
                }

            }

        }



     return  result
    }

    override fun onResume() {
        //chalni code
        super.onResume()
        val supportActionBar: ActionBar? = (requireActivity() as AppCompatActivity).supportActionBar
        if (supportActionBar != null) supportActionBar.hide()

    }
    override fun onStop() {
        super.onStop()
        val supportActionBar: ActionBar? = (requireActivity() as AppCompatActivity).supportActionBar
        if (supportActionBar != null) supportActionBar.show()
    }
}