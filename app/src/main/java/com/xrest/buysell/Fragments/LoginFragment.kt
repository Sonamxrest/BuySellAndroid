package com.xrest.buysell.Fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.KeyguardManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.xrest.buysell.Activity.Dashboard
import com.xrest.buysell.R
import com.xrest.buysell.Retrofit.RetroftiService
import com.xrest.buysell.Retrofit.Repo.UserRepository
import io.socket.client.Socket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.net.URL
import java.util.concurrent.Executor


class LoginFragment : Fragment(), View.OnClickListener {
lateinit var executor: Executor
lateinit var biometricPrompt: BiometricPrompt
lateinit var bio :FloatingActionButton
lateinit var username:TextInputEditText
lateinit var password:TextInputEditText
lateinit var forgot:TextView
lateinit var check: CheckBox
val cb:BiometricPrompt.AuthenticationCallback
get() = @RequiresApi(Build.VERSION_CODES.P)
object :BiometricPrompt.AuthenticationCallback(){

    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
        super.onAuthenticationSucceeded(result)

        var preferences = requireActivity().getSharedPreferences("Login",Activity.MODE_PRIVATE)
        login(preferences.getString("username","")!!,preferences.getString("password","")!!)

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
        var view = inflater.inflate(R.layout.fragment_login, container, false)
        RetroftiService.checkPermission(requireActivity()   )
        var preferences = requireActivity().getSharedPreferences("Login",Activity.MODE_PRIVATE)
        bio = view.findViewById(R.id.bio)
        forgot = view.findViewById(R.id.forgot)
        forgot.setOnClickListener(this)
        bio.isVisible =false
        view.findViewById<LinearLayout>(R.id.bb).isVisible = false
        check = view.findViewById(R.id.remember)
        if(preferences.getBoolean("biometric",false)==true)
        {
            var cancellation = CancellationSignal()
            cancellation.setOnCancelListener(){}
            executor = requireActivity().mainExecutor!!
            bio.isVisible =true
            view.findViewById<LinearLayout>(R.id.bb).isVisible = true

            biometricPrompt = BiometricPrompt.Builder(requireContext()).setTitle("FingerPrint Login").setNegativeButton("Cancel",executor,DialogInterface.OnClickListener(){ dialog, _ ->
                dialog.cancel()
            }).setSubtitle("Place Your Finger To Login").build()
            bio.setOnClickListener(this)
        }
            username  = view.findViewById(R.id.username)
            password  = view.findViewById(R.id.password)
            var sign:TextView = view.findViewById(R.id.sign)
            sign.setOnClickListener(this)
            view.findViewById<FloatingActionButton>(R.id.fb).setOnClickListener(){
            requireActivity().findViewById<ViewPager2>(R.id.vp).currentItem=1
        }

return view
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar!!.hide()
    }

    @SuppressWarnings("MissingPermission")
    override fun onClick(v: View?) {
        when(v?.id)
        {

            R.id.sign -> {

                login(username.text.toString(), password.text.toString())
            }
            R.id.bio ->{
                var cancellation = CancellationSignal()
                cancellation.setOnCancelListener(){}
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                    biometricPrompt.authenticate(cancellation,executor,cb)
                }
            }
            R.id.forgot ->{
                dialog()
            }

        }
    }



    fun  checkBioMetricFeature(){

        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.M)
        {
            var keyGuard = requireContext().getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            if(keyGuard.isKeyguardSecure && requireActivity().packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {

                if (!requireContext().getSharedPreferences("Login", Activity.MODE_PRIVATE)
                        .getBoolean("biometric", false)
                ) {
                    var alert = AlertDialog.Builder(requireContext())
                    alert.setTitle("Do you  want to add fingerprint login feature?")
                        .setMessage("It might be very easy for next time").setPositiveButton(
                            "Yes",
                            DialogInterface.OnClickListener() { _, _ ->
                                var intent = Intent(requireContext(), Dashboard::class.java)
                                startActivity(intent)
                                var editor = requireContext().getSharedPreferences(
                                    "Login",
                                    Activity.MODE_PRIVATE
                                )
                                editor.edit().let { editor ->
                                    editor.putString("username", username.text.toString())
                                    editor.putString("password", password.text.toString())
                                    editor.putBoolean("biometric", true)
                                    editor.apply()
                                    editor.commit()
                                }

                            }).setNegativeButton(
                            "No",
                            DialogInterface.OnClickListener() { dialog, which ->
                                var intent = Intent(requireContext(), Dashboard::class.java)
                                startActivity(intent)
                                dialog.cancel()
                            })

                    var dialog = alert.create()
                    dialog.setIcon(R.drawable.ic_baseline_fingerprint_24)
                    dialog.setCancelable(false)
                    dialog.show()

                }
                else{
                    var intent = Intent(requireContext(), Dashboard::class.java)
                    startActivity(intent)
                }
            }
            else{
                var intent = Intent(requireContext(), Dashboard::class.java)
                startActivity(intent)
            }

        }
        else{
            var intent = Intent(requireContext(), Dashboard::class.java)
            startActivity(intent)
        }

        Toast.makeText(requireContext(), "Pass", Toast.LENGTH_SHORT).show()

    }

    fun login(usernames:String,passwords:String){
        CoroutineScope(Dispatchers.IO).launch {

            var response =
                UserRepository().login(usernames,passwords)
            if (response.success == true) {
                withContext(Main)
                {

                    RetroftiService.token ="Bearer "+ response.token!!
                    RetroftiService.users = response.user!!
                    if (check.isChecked) {
                        Toast.makeText(requireContext(), "Username and Password is saved", Toast.LENGTH_SHORT).show()
                        var editor = requireContext().getSharedPreferences(
                            "userLogin",
                            Activity.MODE_PRIVATE
                        )
                        editor.edit().let { editor ->
                            editor.putString("username", username.text.toString())
                            editor.putString("password", password.text.toString())
                            editor.apply()
                            editor.commit()
                        }
                    }
                    checkBioMetricFeature()

                }
            } else {
                withContext(Main)
                {
                    username.setError("Invalid Credentials")
                    password.setError("Invalid Credentials")
                }
            }


        }
    }



    var count =0
    var index =0
    var points =0

    fun dialog(){
        var dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.forget_password)
        dialog.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        var phone = dialog.findViewById<EditText>(R.id.phone)
        var question = dialog.findViewById<TextView>(R.id.question)
        var answer =dialog.findViewById<EditText>(R.id.answer)
        var password = dialog.findViewById<EditText>(R.id.password)
        var cpassword = dialog.findViewById<EditText>(R.id.cpassword)
        var next:Button = dialog.findViewById(R.id.next)
        phone.isVisible=false
        question.isVisible=false
        answer.isVisible=false
        password.isVisible=false
        cpassword.isVisible=false
        Log.d("points",points.toString())
        Log.d("index",index.toString())
        Log.d("count",count.toString())
        when(count){
            0 -> {
                phone.isVisible = true
                next.setOnClickListener() {

                    CoroutineScope(Dispatchers.IO).launch {
                        var response = UserRepository().getUser(phone.text.toString())
                        if (response.success == true) {
                            RetroftiService.users = response.user
                            withContext(Main) {
                                count += 1
                                dialog.cancel()
                                dialog()
                            }
                        } else {
                            withContext(Main)
                            {
                                dialog.cancel()
                                Toast.makeText(requireContext(), "No user Found With This Username", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

            }
            1->{
                var qa= RetroftiService.users!!.Questions
                question.isVisible =true
                answer.isVisible=true

                    question.text =  qa?.get(index)!!.question
                    next.setOnClickListener(){

                        if(answer.text.toString().toLowerCase().equals(qa[index].answer!!.toLowerCase()))
                        {
                            points+=1
                            Log.d("milyo","milyo")
                        }

                        index+=1

                        if(index ==3 && points==3){
                            count+=1
                            dialog.cancel()
                             dialog()
                            index =0
                            count=0
                            points=0
                        }
                        else if(index==3 && points<3)
                        {
                            dialog.cancel()
                            index =0
                            count=0
                            points=0
                        }else{
                            dialog.cancel()
                            dialog()
                        }


                }



            }
            2 -> {
                password.isVisible = true
                cpassword.isVisible = true
                next.setOnClickListener() {
                    if (password.text.toString() != cpassword.text.toString()) {
                        password.setError("Password Did not matched")
                    } else {
                        try{
                            CoroutineScope(Dispatchers.IO).launch {
                                var reponse = UserRepository().updatePassword(
                                    password.text.toString(),
                                    RetroftiService.users!!._id!!
                                )
                                if (reponse.success == true) {
                                    withContext(Main)
                                    {
                                        dialog.cancel()
                                        Toast.makeText(requireContext(), "Password Changed Successfully", Toast.LENGTH_LONG).show()
                                    }
                                } else {
                                    withContext(Main)
                                    {
                                        dialog.cancel()
                                        Toast.makeText(requireContext(), "OPPS!! Something Went Wrong", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                        }
                        catch (ex:Exception)
                        {
                            ex.printStackTrace()

                        }
                    }
                }
            }

        }


        dialog.show()
        dialog.setCancelable(false)



    }

}




