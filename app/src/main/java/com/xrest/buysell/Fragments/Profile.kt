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
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.xrest.buysell.R
import com.xrest.buysell.Retrofit.Repo.UserRepository
import com.xrest.buysell.Retrofit.RetroftiService
import com.xrest.buysell.Retrofit.User
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
lateinit var layout:LinearLayout
val user =RetroftiService.users

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_profile, container, false)
        name = view.findViewById(R.id.name)
        layout= view.findViewById(R.id.container)
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
                                    Toast.makeText(requireContext(), "Updated", Toast.LENGTH_SHORT).show()
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
                profile.setImageBitmap(BitmapFactory.decodeFile(image))
                   uploadImage(image)

        }
            else if(requestCode==1&&data!=null)
            {
                var image = RetroftiService.getDataFromCamera(requireContext(),data)
                profile.setImageBitmap(BitmapFactory.decodeFile(image))
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
            CoroutineScope(Dispatchers.IO).launch {

                var response = UserRepository().updateProfile(RetroftiService.users!!._id!!,body)
                if(response.success==true)
                {
                    Snackbar.make(layout,"Profile Updated",Snackbar.LENGTH_LONG)
                }

            }

        }
        catch (ex:Exception){


        }
    }
    }

