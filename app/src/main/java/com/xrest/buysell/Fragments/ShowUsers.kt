package com.xrest.buysell.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xrest.buysell.Adapters.UserAdapters
import com.xrest.buysell.R
import com.xrest.buysell.Retrofit.Person
import com.xrest.buysell.Retrofit.Repo.UserRepository
import com.xrest.buysell.Retrofit.RetroftiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ShowUsers : Fragment() {

var lst:MutableList<Person> = mutableListOf()
    lateinit var rv:RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view=  inflater.inflate(R.layout.fragment_show_users, container, false)
        rv= view.findViewById(R.id.rv)
        rv.layoutManager = LinearLayoutManager(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            var reponse = UserRepository().showAll()
            if(reponse.success==true)
            {
                withContext(Main)
                {

                    for(data in reponse.data!!)
                    {
                    if(data._id!!.equals(RetroftiService.users!!._id))
                    {

                    }
                        else{
                            lst.add(data)
                        }
                    }
                    var adapters = UserAdapters(lst,requireContext())
                    rv.adapter =adapters

                }
            }
        }
        return view
    }


    override fun onResume() {

        var actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar!!.show()
        actionBar.setTitle("")
        super.onResume()
    }

}