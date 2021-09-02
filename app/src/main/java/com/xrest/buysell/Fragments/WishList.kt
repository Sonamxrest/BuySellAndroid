package com.xrest.buysell.Fragments

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
import java.util.concurrent.Executor


class WishList : Fragment() {
lateinit var rv:RecyclerView
lateinit var cl:CoordinatorLayout
lateinit var pay: Button
    lateinit var executor: Executor
    lateinit var biometricPrompt: BiometricPrompt
    lateinit var bio : FloatingActionButton
    lateinit var password:EditText
lateinit var  adapter:WishListAdapter
var lst:MutableList<Productss> = mutableListOf()
    val cb:BiometricPrompt.AuthenticationCallback
        get() = @RequiresApi(Build.VERSION_CODES.P)
        object :BiometricPrompt.AuthenticationCallback(){

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                super.onAuthenticationSucceeded(result)

                var preferences = requireActivity().getSharedPreferences("Login", Activity.MODE_PRIVATE)
                password.setText(preferences.getString("passowrd",""))

            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(requireContext(), "Invalid Finger Print Detected", Toast.LENGTH_LONG).show()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(requireContext(), "Sensor Might be blocked Plese clear and try again", Toast.LENGTH_LONG).show()

            }


        }
    @RequiresApi(Build.VERSION_CODES.P)
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
                    var dialog = Dialog(requireContext())
                    dialog.setContentView(R.layout.dealsewalogin)
                    dialog.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
                    dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
                    password = dialog.findViewById(R.id.password)
                    var login :Button = dialog.findViewById(R.id.login)
                    bio = dialog.findViewById(R.id.bio)
                    bio.isVisible =false
                    var preferences = requireActivity().getSharedPreferences("Login",Activity.MODE_PRIVATE)
                    if(preferences.getBoolean("biometric",false)==true)
                    {
                        var cancellation = CancellationSignal()
                        cancellation.setOnCancelListener(){}
                        executor = requireActivity().mainExecutor!!
                        bio.isVisible =true
                        view.findViewById<LinearLayout>(R.id.bb).isVisible = true

                        biometricPrompt = BiometricPrompt.Builder(requireContext()).setTitle("FingerPrint Login").setNegativeButton("Cancel",executor,
                            DialogInterface.OnClickListener(){ dialog, _ ->
                            dialog.cancel()
                        }).setSubtitle("Place Your Finger To Login").build()
                        bio.setOnClickListener(){
                            var cancellation = CancellationSignal()
                            cancellation.setOnCancelListener(){}
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                                biometricPrompt.authenticate(cancellation,executor,cb)
                            }
                        }
                    }
                    login.setOnClickListener(){
                        try {
                            CoroutineScope(Dispatchers.IO).launch {
                                var response = UserRepository().check(password.text.toString())
                                if(response.success == true)
                                {
                                    withContext(Main)
                                    {
                                        dialog.cancel()
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
                                            if(amount.text.isNullOrBlank() || amount.text.toString().toInt() > 100)
                                            {
                                                amount.setError("")
                                            }
                                            else{
                                                bottomSheet.cancel()
                                                var dialogs = Dialog(requireContext())
                                                dialogs.setContentView(R.layout.success_notification)
                                                dialogs.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
                                                var lottie = dialogs.findViewById<LottieAnimationView>(R.id.lottie)
                                                var success = dialogs.findViewById<TextView>(R.id.success)
                                                success.text="Uploading"
                                                dialogs.show()
                                                dialogs.setCancelable(false)
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
                                                            flag = response.success == true && response2.success == false && response3.success == true
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
                                                            dialogs.cancel()
                                                        }
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
                                else{
                                    withContext(Main)
                                    {
                                        Toast.makeText(requireContext(), "Password Did not matched", Toast.LENGTH_SHORT).show()
                                        dialog.cancel()
                                    }
                                }
                            }
                        }
                        catch (ex:Exception){

                        }
                    }
                    dialog.show()
              dialog.setCancelable(false)

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