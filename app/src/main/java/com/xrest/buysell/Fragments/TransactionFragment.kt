package com.xrest.buysell.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
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
   lateinit var lsst:MutableList<Transaction>

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
                    lsst = response.data!!
                    print(response.data.toString())
                }
            }
        }
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = BillAdapter(requireContext(),send())
        view.findViewById<Button>(R.id.sent).setOnClickListener(){
            rv.adapter =BillAdapter(requireContext(),send())
        }
        view.findViewById<Button>(R.id.recieved).setOnClickListener(){
            rv.adapter =BillAdapter(requireContext(),rec())
        }
        return view
    }

    fun send(): MutableList<Transaction>{
        var newList = mutableListOf<Transaction>()
        for(data in lsst){
            if(data.Sender?._id == RetroftiService.users!!._id)
            {
                newList.add(data)
            }
        }
        return newList
    }
    fun rec(): MutableList<Transaction>{
        var newList = mutableListOf<Transaction>()
        for(data in lsst){
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