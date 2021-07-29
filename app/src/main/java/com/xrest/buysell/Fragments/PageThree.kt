package com.xrest.buysell.Fragments

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import com.xrest.buysell.R

class PageThree : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view=  inflater.inflate(R.layout.fragment_page_three, container, false)
        view.findViewById<TextView>(R.id.finish).setOnClickListener(){
            var editor = requireActivity().getSharedPreferences("onBoarding",Activity.MODE_PRIVATE).edit()
            editor.putBoolean("Boarding",true)
            editor.apply()
            editor.commit()
            Navigation.findNavController(view).navigate(R.id.action_startActions_to_loginSignup)

        }
        return view
    }


}