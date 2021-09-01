package com.xrest.buysell.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.xrest.buysell.Adapters.BillAdapter
import com.xrest.buysell.R
import com.xrest.buysell.Retrofit.Class.Transaction
import com.xrest.buysell.Retrofit.Repo.UserRepository
import com.xrest.buysell.Retrofit.RetroftiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class TransactionFragment : Fragment() {
    var lst = mutableListOf<Transaction>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.fragment_transaction, container, false)
        var rv:RecyclerView = view.findViewById(R.id.rv)
        var res:TextView = view.findViewById(R.id.res)
        res.isVisible = false
        CoroutineScope(Dispatchers.IO).launch {
            var response = UserRepository().tr()
            if(response.success == true)
            {
                withContext(Main){
                    lst = response.data!!
                }
            }
        }
        if(lst.size==0)
            {
            res.isVisible = true
        }
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = BillAdapter(requireContext(),send())
        var bottomNav :BottomNavigationView = view.findViewById(R.id.nav)
        bottomNav.setOnNavigationItemSelectedListener {
            when(it.itemId)
            {
                R.id.sent->{
                    rv.adapter = BillAdapter(requireContext(),send())
                }
                R.id.recieve ->{
                    rv.adapter = BillAdapter(requireContext(),rec())
                }
            }
            true
        }

        return view
    }

    fun send(): MutableList<Transaction>{
        var newList = mutableListOf<Transaction>()
        for(data in lst){
            if(data.Sender?._id == RetroftiService.users!!._id)
            {
                newList.add(data)
            }
        }
        return newList
    }
    fun rec(): MutableList<Transaction>{
        var newList = mutableListOf<Transaction>()
        for(data in lst){
            if(data.Reciever?._id == RetroftiService.users!!._id)
            {
                newList.add(data)
            }
        }
        return newList
    }

    override fun onResume() {
        super.onResume()
        (requireContext() as AppCompatActivity).supportActionBar!!.hide()
    }
}