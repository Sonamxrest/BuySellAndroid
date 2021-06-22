package com.xrest.buysell

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.widget.PopupWindowCompat
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.lottie.LottieAnimationView
import com.xrest.buysell.Retrofit.Question
import com.xrest.buysell.Retrofit.RetroftiService
import com.xrest.buysell.Retrofit.User
import com.xrest.buysell.Retrofit.UserRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.lang.Exception
import java.net.Inet4Address


class Signup : Fragment(), View.OnClickListener {
    var question = mutableListOf("What is lucky number ?"
        ,"What is your favourite food ?"
        ,"Where is your favourite place to visit?",
    "What is your favourite Drink ?",
    "Your Birth Month?")
lateinit var name:EditText
    lateinit var phone:EditText
    lateinit var username:EditText
    lateinit var password:EditText
    lateinit var cpassword:EditText
    lateinit var register:TextView
    lateinit var profile:ImageView
    private var image:String?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_signup, container, false)
        initialize(view)
        register.setOnClickListener(this)
        profile.setOnClickListener(this)

        return view
    }

    private fun showPopup() {
        val popUp = PopupMenu(requireContext(),profile)
        popUp.menuInflater.inflate(R.menu.camera_gallery,popUp.menu)
            try{
                val popup = PopupMenu::class.java.getDeclaredField("mPopup")
                popup.isAccessible = true
                val menu = popup.get(popUp)
                menu.javaClass
                    .getDeclaredMethod("setForceShowIcon",Boolean::class.java)
                    .invoke(menu,true)
            }
            catch (ex:Exception)
            { ex.printStackTrace()
            } finally {
                popUp.show()
                popUp.setOnMenuItemClickListener {
                    when(it.itemId)
                    {
                        R.id.camera->{

                            var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            startActivityForResult(intent,1)

                        }
                        R.id.gallery->{
                         var intent = Intent(Intent.ACTION_PICK)
                         intent.type="image/*"
                         startActivityForResult(intent,0)
                        }
                    }
                    true
                }

            }


    }

    private fun initialize(view: View) {
        name = view.findViewById(R.id.name)
        phone = view.findViewById(R.id.phone)
        username = view.findViewById(R.id.username)
        password = view.findViewById(R.id.password)
        cpassword = view.findViewById(R.id.cpassword)
        register = view.findViewById(R.id.register)
        profile = view.findViewById(R.id.profile)
    }

    fun validate(edt:MutableList<EditText>):Boolean{
       for(data in edt)
       {
           if(data.text.toString().length ==0)
           {
               when(data.id)
               {
                   R.id.name-> data.setError("Please Enter Your Name")
                   R.id.username -> data.setError("Please Enter Your Username")
                   R.id.password -> data.setError("Please Enter Your Password")
                   R.id.phone -> data.setError("Please Enter Your Number")
                   R.id.cpassword -> data.setError("Please Enter Your Password")

               }
           }
           else{


               when(data.id)
               {
                   R.id.password->{
                       if(data.text.length<8)
                       {
                           data.setError("Password must be long than 8 letters")
                       }
                   }
               }

           }
       }
       return true
   }


    override fun onClick(v: View?) {
        when(v?.id)
        {
            R.id.register -> {
                var qa:MutableList<Question> = mutableListOf()
dialog(qa)

            }
            R.id.profile->{
                showPopup()

            }
        }
    }
    var count =0
    private fun dialog(qa: MutableList<Question>) :Boolean{
        var q = ""
        var p = 0
        var result :Boolean = false
        var dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.questions)
        dialog.window!!.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        var spinner = dialog.findViewById<Spinner>(R.id.spinner)
        var answer = dialog.findViewById<EditText>(R.id.answer)
        var next = dialog.findViewById<Button>(R.id.next)
        spinner.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, question)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                q = parent!!.getItemAtPosition(position).toString()
                p = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.show()
        dialog.setCancelable(true)
        dialog.window!!.setGravity(Gravity.CENTER)

        next.setOnClickListener() {
            qa.add(Question(q, answer.text.toString()))
            question.removeAt(p)
            dialog.cancel()
            count +=1
            Log.d("coubtsasdasdas",count.toString())
            if (count == 3)
            {

                var lst: MutableList<EditText> = mutableListOf()
                lst.add(name)
                lst.add(phone)
                lst.add(username)
                lst.add(password)
                lst.add(cpassword)
                if (validate(lst)) {

                    if (password.text.toString() == cpassword.text.toString()) {


                        var user = User(
                            Name = name.text.toString(),
                            PhoneNumber = phone.text.toString(),
                            Username = username.text.toString(),
                            Password = password.text.toString(),
                            Questions = qa
                        )

                        CoroutineScope(Dispatchers.IO).launch {

                            var response = UserRepository().insert(user)
                            if (response.success == true) {
                                if (image == null) {
                                    withContext(Main)
                                    {
                                        Toast.makeText(requireContext(), "Registrataion Successful", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    withContext(Main)
                                    {
                                        uploadImage(response.message!!,image!!)

                                    }


                                }
                            }

                        }



                    } else {
                        cpassword.setError("Password Incorrect")
                        Toast.makeText(
                            requireContext(),
                            "Password did not matched",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                else{
                    Toast.makeText(
                        requireContext(),
                        "Password did  matched",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                result =true
            }else{
                dialog(qa)
                result =false
            }
        }
        return result
    }

    private fun uploadImage(message: String, image: String) {

        try {
            var file = File(image)
            var extention = MimeTypeMap.getFileExtensionFromUrl(image)
            var mimetype = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extention)
            var requestBody = RequestBody.create(MediaType.parse(mimetype),file)
            var body = MultipartBody.Part.createFormData("profile",file.name,requestBody)
            var dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.success_notification)
            dialog.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            var lottie = dialog.findViewById<LottieAnimationView>(R.id.lottie)
            var success = dialog.findViewById<TextView>(R.id.success)
            success.text="Uploading"
            dialog.show()
            dialog.setCancelable(false)
            CoroutineScope(Dispatchers.IO).launch {

                val response = UserRepository().updateProfile(message,body)

                if(response.success==true)
                {
                    withContext(Main){
                        lottie.setAnimation(R.raw.success)
                        lottie.loop(true)
                        lottie.playAnimation()
                        success.text = "Complete"
                        dialog.setCancelable(true)

                    }
                    delay(2000)
                    withContext(Main)
                    {
                    requireActivity().findViewById<ViewPager2>(R.id.vp).currentItem=0
                    }
                }

            }


        }
        catch (ex:Exception)
        {
            ex.printStackTrace()
        }



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==Activity.RESULT_OK &&data!=null)
        {
            if(requestCode==0 && data !=null)
            {
                image = RetroftiService.getDataFromGallery(requireContext(),data)
                profile.setImageBitmap(BitmapFactory.decodeFile(image))
                Log.d("noImage", image!!)
            }
            else if(requestCode == 1 && data !=null)
            {
                image = RetroftiService.getDataFromCamera(requireContext(),data)
                profile.setImageBitmap(BitmapFactory.decodeFile(image))
                Log.d("noImage", image!!)

            }
        }
    }


}