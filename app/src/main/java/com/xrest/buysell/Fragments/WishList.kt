package com.xrest.buysell.Fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.xrest.buysell.Adapters.WishListAdapter
import com.xrest.buysell.R
import com.xrest.buysell.Retrofit.Productss
import com.xrest.buysell.Retrofit.Repo.UserRepository
import com.xrest.buysell.SwipeToDeleteCallback
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main


class WishList : Fragment() {
lateinit var rv:RecyclerView
lateinit var cl:CoordinatorLayout
lateinit var  adapter:WishListAdapter
var lst:MutableList<Productss> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_wish_list, container, false)
        cl = view.findViewById(R.id.coordinatorLayout)
        rv = view.findViewById(R.id.rv)
        rv.layoutManager =LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        try {
            CoroutineScope(Dispatchers.IO).launch {
                var response = UserRepository().getWish()
                if(response.success==true)
                {
                    withContext(Main)
                    {
                        Log.d("data", response.user?.Likes.toString())
                        if(response.user!!.Likes!!.size >0)
                        {
                            lst = response.user!!.Likes!!
                            adapter=WishListAdapter(lst, requireContext())
                            rv.adapter = adapter
                            enableSwipeToDeleteAndUndo()
                        }
                    }
                }
            }
        }
        catch (ex: Exception)
        {
            ex.printStackTrace()
        }

        return view
    }
    private fun enableSwipeToDeleteAndUndo() {
        val swipeToDeleteCallback: SwipeToDeleteCallback = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
                val position = viewHolder.adapterPosition
                val item: Productss = adapter.getData().get(position)
                adapter.removeItem(position)
                val snackbar = Snackbar
                    .make(
                        cl,
                        "Item was removed from the list.",
                        Snackbar.LENGTH_LONG
                    )
                snackbar.setAction("UNDO") {
                    adapter.restoreItem(item, position)
                    rv.scrollToPosition(position)
                }
                snackbar.setActionTextColor(Color.YELLOW)
                snackbar.show()
            }
        }
        val itemTouchhelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchhelper.attachToRecyclerView(rv)
    }

}