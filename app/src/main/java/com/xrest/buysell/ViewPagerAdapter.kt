package com.xrest.buysell

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(var lst:MutableList<Fragment>,var manager:FragmentManager,var lifecycle: Lifecycle):FragmentStateAdapter(manager,lifecycle) {
    override fun getItemCount(): Int {
        return lst.size
    }

    override fun createFragment(position: Int): Fragment {
       return lst[position]
    }
}