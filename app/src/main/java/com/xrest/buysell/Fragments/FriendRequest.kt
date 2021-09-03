package com.xrest.buysell.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xrest.buysell.Adapters.RequestAdapter
import com.xrest.buysell.R
import com.xrest.buysell.Retrofit.Repo.RequestRepo
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FriendRequest : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view= inflater.inflate(R.layout.fragment_friend_request, container, false)
        val rv: RecyclerView = view.findViewById(R.id.rv)

        CoroutineScope(Dispatchers.IO).launch {
            val response = RequestRepo().getRequest()
            if(response.success==true)
            {
              withContext(Main)
              {
                  rv.layoutManager = LinearLayoutManager(requireContext())
                  rv.adapter=RequestAdapter(response.data!!,requireContext())
              }
            }
        }


        return view
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.title=""
    }
}