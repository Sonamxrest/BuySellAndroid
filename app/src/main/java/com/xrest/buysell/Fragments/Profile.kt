package com.xrest.buysell.Fragments

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaParser
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.xrest.buysell.Adapters.MainProductAdapter
import com.xrest.buysell.R
import com.xrest.buysell.Retrofit.Product
import com.xrest.buysell.Retrofit.Repo.ProductRepo
import com.xrest.buysell.Retrofit.Repo.UserRepository
import com.xrest.buysell.Retrofit.RetroftiService
import com.xrest.buysell.Retrofit.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import okhttp3.Dispatcher
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.lang.Exception


class Profile : Fragment(), View.OnClickListener {

lateinit var name:TextView
lateinit var number:TextView
lateinit var username:TextView
lateinit var profile: CircleImageView
lateinit var update:FloatingActionButton

lateinit var rv:RecyclerView
val user =RetroftiService.users
    var adapter = GroupAdapter<GroupieViewHolder>()
    var lst:MutableList<Product> =mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_profile, container, false)
        rv = view.findViewById(R.id.rv)
        rv.layoutManager = LinearLayoutManager(requireContext())
        loadData(view)
        name = view.findViewById(R.id.name)
        number =view.findViewById(R.id.number)
        profile=view.findViewById(R.id.profile)
        username = view.findViewById(R.id.username)
        update = view.findViewById(R.id.update)
        name.text = user!!.Name
        number.text = user.PhoneNumber
        username.text = user.Username
        Glide.with(requireContext()).load(RetroftiService.loadImage(user.Profile!!)).into(profile)
        update.setOnClickListener(this)
        profile.setOnClickListener(this)
        return view
    }

    override fun onClick(v: View?) {
        when(v?.id)
        {
            R.id.update -> {
                var dialog = Dialog(requireContext())
                dialog.setContentView(R.layout.update_profile)
                dialog.window!!.setLayout(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
                dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
                var name: TextInputEditText = dialog.findViewById(R.id.name)
                var number: TextInputEditText = dialog.findViewById(R.id.number)
                var profile: CircleImageView = dialog.findViewById(R.id.profile)
                var username: TextInputEditText = dialog.findViewById(R.id.username)
                var update: Button = dialog.findViewById(R.id.update)
                name.setText(user!!.Name)
                number.setText(user.PhoneNumber)
                username.setText(user.Username)
                Glide.with(requireContext()).load(RetroftiService.loadImage(user.Profile!!))
                    .into(profile)
                update.setOnClickListener() {
                    var dialogs = Dialog(requireContext())
                    dialogs.setContentView(R.layout.success_notification)
                    dialogs.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
                    var lottie = dialogs.findViewById<LottieAnimationView>(R.id.lottie)
                    var success = dialogs.findViewById<TextView>(R.id.success)
                    success.text="Uploading"
                    dialogs.show()
                    dialogs.setCancelable(false)
                    try {
                        CoroutineScope(Dispatchers.IO).launch {
                            var user = User(
                                Name = name.text.toString(),
                                Username = username.text.toString(),
                                PhoneNumber = number.text.toString()
                            )
                            var response = UserRepository().updateDetails(user)
                            if (response.success == true) {
                                withContext(Main) {
                                    lottie.setAnimation(R.raw.success)
                                    lottie.loop(true)
                                    lottie.playAnimation()
                                    success.text = "Profile Updated"
                                    RetroftiService.users!!.Name = user.Name
                                    RetroftiService.users!!.PhoneNumber =user.PhoneNumber
                                    RetroftiService.users!!.Username =user.Username
                                    dialogs.cancel()

                                }
                            }
                        }


                    } catch (ex: Exception) {

                    }
                }
                dialog.show()
                dialog.setCancelable(true)


            }
            R.id.profile->{
                var popupMenu =PopupMenu(requireContext(),profile)
                popupMenu.menuInflater.inflate(R.menu.camera_gallery,popupMenu.menu)
                popupMenu.show()
                popupMenu.setOnMenuItemClickListener {
                    when(it.itemId)
                    {
                        R.id.gallery->{
                            var intent = Intent(Intent.ACTION_PICK)
                            intent.type ="image/*"
                            startActivityForResult(intent,0)
                        }
                        R.id.camera->{
                            var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            startActivityForResult(intent,1)
                        }

                    }
                    true
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode==Activity.RESULT_OK && data!=null)
        {

            if(requestCode==0 && data !=null)
            {
                var image = RetroftiService.getDataFromGallery(requireContext(),data)

                   uploadImage(image)

        }
            else if(requestCode==1&&data!=null)
            {
                var image = RetroftiService.getDataFromCamera(requireContext(),data)

                uploadImage(image)
            }
    }

}

    private fun uploadImage(image: String?) {
        try {


            var file = File(image)
            var extention = MimeTypeMap.getFileExtensionFromUrl(image)
            var mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extention)
            var reqBody = RequestBody.create(MediaType.parse(mimeType),file)
            var body = MultipartBody.Part.createFormData("profile",file.name,reqBody)
            var dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.success_notification)
            dialog.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            var lottie = dialog.findViewById<LottieAnimationView>(R.id.lottie)
            var success = dialog.findViewById<TextView>(R.id.success)
            success.text="Uploading"
            dialog.show()
            dialog.setCancelable(false)
            CoroutineScope(Dispatchers.IO).launch {

                var response = UserRepository().updateProfile(RetroftiService.users!!._id!!,body)
                if(response.success==true)
                {
                    withContext(Main){
                        lottie.setAnimation(R.raw.success)
                        lottie.loop(true)
                        lottie.playAnimation()
                        success.text = "Profile Updated"
                        RetroftiService.users!!.Profile = response.message!!
                        dialog.setCancelable(true)

                    }
                    delay(2000)
                    withContext(Main){
                        dialog.cancel()
                    }
                }

            }

        }
        catch (ex:Exception){


        }
    }
    fun loadData(view:View){


//        var dialog =Dialog(requireContext())
//        dialog.setContentView(R.layout.success_notification)
//        dialog.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
//        dialog.window!!.attributes!!.windowAnimations = R.style.DialogAnimation
//        dialog.show()
//        dialog.setCancelable(false)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                var response = ProductRepo().personsPost(RetroftiService!!.users!!._id!!)
                if(response.success==true)
                {
                    withContext(Main)
                    {
                        lst = response.data!!
                        addToAdapter(lst)
                        //dialog.cancel()
                        rv.adapter =adapter
                    }
                }
            }
            catch (ex:Exception)
            {

            }
        }
    }
    fun addToAdapter(lst:MutableList<Product>)
    {
        adapter.clear()
        for(data in lst)
        {
            adapter.add(MainProductAdapter(requireContext(),data))
        }
    }

    }

