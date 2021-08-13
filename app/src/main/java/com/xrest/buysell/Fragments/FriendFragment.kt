package com.xrest.buysell.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xrest.buysell.Adapters.FriendsAdapter
import com.xrest.buysell.R
import com.xrest.buysell.Retrofit.Product
import com.xrest.buysell.Retrofit.Repo.UserRepository
import com.xrest.buysell.Retrofit.friend
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FriendFragment : Fragment() {

    var lst:MutableList<friend> =mutableListOf()
    var slst:MutableList<friend> =mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_friends, container, false)
        val rv:RecyclerView = root.findViewById(R.id.rv)
        rv.layoutManager = LinearLayoutManager(requireContext())
        val adapter = GroupAdapter<GroupieViewHolder>()
        (requireActivity() as AppCompatActivity).supportActionBar!!.title =""
        CoroutineScope(Dispatchers.IO).launch{

            val repo = UserRepository()
            val response = repo.showFriends()
            if(response.success==true){

                withContext(Main){
                for(data in response.data?.Friends!!)
                {
                    adapter.add(FriendsAdapter(data,requireContext()))
                }


                }


            }


        }
        rv.adapter=adapter

        return root
    }

}