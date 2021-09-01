package com.xrest.buysell.Fragments

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.xrest.buysell.Activity.user
import com.xrest.buysell.Adapters.PayUserAdapter
import com.xrest.buysell.Adapters.WishListAdapter
import com.xrest.buysell.R
import com.xrest.buysell.Retrofit.Person
import com.xrest.buysell.Retrofit.Productss
import com.xrest.buysell.Retrofit.Repo.ProductRepo
import com.xrest.buysell.Retrofit.Repo.UserRepository
import com.xrest.buysell.Retrofit.RetroftiService
import com.xrest.buysell.SwipeToDeleteCallback
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main


class WishList : Fragment() {
lateinit var rv:RecyclerView
lateinit var cl:CoordinatorLayout
lateinit var pay: Button
lateinit var  adapter:WishListAdapter
var lst:MutableList<Productss> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_wish_list, container, false)
        pay = view.findViewById(R.id.pay)
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

                pay.setOnClickListener(){
                    var bottomSheet = Dialog(requireContext())
                    bottomSheet.setContentView(R.layout.addtocart)
                    bottomSheet.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
                    var rv:RecyclerView = bottomSheet.findViewById(R.id.rv)
                    var money:TextView = bottomSheet.findViewById(R.id.money)
                    var image:ImageView = bottomSheet.findViewById(R.id.imageView2)
                    var amount:TextInputEditText = bottomSheet.findViewById(R.id.amount)
                    var desc = bottomSheet.findViewById<TextInputEditText>(R.id.desc)
                    var button = bottomSheet.findViewById<Button>(R.id.button)
                    button.setOnClickListener(){
                        bottomSheet.cancel()
                        var dialog = Dialog(requireContext())
                        dialog.setContentView(R.layout.success_notification)
                        dialog.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
                        var lottie = dialog.findViewById<LottieAnimationView>(R.id.lottie)
                        var success = dialog.findViewById<TextView>(R.id.success)
                        success.text="Uploading"
                        dialog.show()
                        dialog.setCancelable(false)
CoroutineScope(Dispatchers.IO).launch {
    var i=0
    var flag = false
    for(i in 0..lst.size -1)
    {
        if(lst[i].product!!.User!!._id !=RetroftiService.users!!._id)
        {
            var response = UserRepository().pay(lst[i].product!!.User!!._id.toString(),amount.text.toString(),desc.text.toString(),lst[i].product?._id!!)
            var response2 = ProductRepo().Like(lst[i].product?._id!!)
            var response3 = ProductRepo().sold(lst[i].product?._id!!)
            flag = response.success == true && response2.success == true && response3.success == true
        }


    }
    withContext(Main){
        if(flag==true)
        {
            lottie.setAnimation(R.raw.success)
            lottie.loop(true)
            lottie.playAnimation()
            success.text = "Complete"
            amount.setText(null)
            desc.setText(null)
            dialog.cancel()
        }
    }
}
                    }
                    var userList = mutableListOf<Person>()
                    for(data in lst)
                    {
                        userList.add(data.product?.User!!)
                    }
                    money.text = RetroftiService.users?.Cash.toString()
                    Glide.with(requireContext()).load(RetroftiService.loadImage(RetroftiService.users?.Profile!!)).into(image)

                    rv.layoutManager = GridLayoutManager(requireContext(),3,GridLayoutManager.VERTICAL,false)
                    rv.adapter = PayUserAdapter(requireContext(), userList)
                    bottomSheet.show()
                    bottomSheet.setCancelable(true)

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

    override fun onResume() {
        (requireContext() as AppCompatActivity).supportActionBar!!.hide()
        super.onResume()
    }
}