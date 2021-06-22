package com.xrest.buysell

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2


class LoginSignup : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_login_signup, container, false)
        var vp:ViewPager2 = view.findViewById(R.id.vp)
        vp.adapter =ViewPagerAdapter(mutableListOf(LoginFragment(),Signup()),requireFragmentManager(),lifecycle)
        return view
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar!!.hide()
    }

}