package com.xrest.buysell.Fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.textfield.TextInputEditText
import com.xrest.buysell.Activity.adapter
import com.xrest.buysell.Adapters.MainProductAdapter
import com.xrest.buysell.Adapters.UserAdapters
import com.xrest.buysell.R
import com.xrest.buysell.Retrofit.Person
import com.xrest.buysell.Retrofit.Product
import com.xrest.buysell.Retrofit.Repo.UserRepository
import com.xrest.buysell.Retrofit.RetroftiService
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.*


class ShowUsers : Fragment(), View.OnClickListener {

var lst:MutableList<Person> = mutableListOf()
    var slst:MutableList<Person> = mutableListOf()
    lateinit var mic: ImageButton
    lateinit var search: TextInputEditText
    lateinit var swipe: SwipeRefreshLayout
    lateinit var rv:RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view=  inflater.inflate(R.layout.fragment_show_users, container, false)
       loadData(view);
        rv= view.findViewById(R.id.rv)
        mic = view.findViewById(R.id.mic)
        swipe = view.findViewById(R.id.swipe)
        search = view.findViewById(R.id.search)
        swipe.setOnRefreshListener(){
            loadData(view)
            swipe.isRefreshing=false
        }
        rv.layoutManager = LinearLayoutManager(requireContext())
        mic.setOnClickListener(this)
        search.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

                if(search.text.toString().isNotEmpty())
                {
                    slst.clear()
                    for(data in lst)
                    {
                        if(data.Name!!.toLowerCase().contains(search.text.toString().toLowerCase()))
                        {
                            slst.add(data)
                        }
                    }

                  rv.adapter = UserAdapters(slst,requireContext())
                }
                else{
                    rv.adapter = UserAdapters(lst,requireContext())

                }

            }
        })


        return view
    }

    private fun loadData(view: View) {
        try {
            view.findViewById<ShimmerFrameLayout>(R.id.shimmer).isVisible = true
            CoroutineScope(Dispatchers.IO).launch {
                var reponse = UserRepository().showAll()
                if (reponse.success == true) {
                    withContext(Main)
                    {

                        for (data in reponse.data!!) {
                            if (data._id != RetroftiService.users!!._id) {
                                for (ddata in RetroftiService!!.users!!.Friends!!) {
                                    if (ddata.user._id != data._id) {
                                        lst.add(data)
                                    }
                                }

                            }
                            view.findViewById<ShimmerFrameLayout>(R.id.shimmer).isVisible = false
                            var adapters = UserAdapters(lst, requireContext())
                            rv.adapter = adapters

                        }
                    }
                }
            }
        }
            catch(ex:Exception) {

            }

    }


    override fun onResume() {

        var actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar!!.show()
        actionBar.setTitle("")
        super.onResume()
    }

    override fun onClick(v: View?) {
        when(v?.id)
        {
            R.id.mic ->{

                var intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Please Speak Now")
                startActivityForResult(intent,5)

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK&&data!=null)
        {

            if(requestCode==5 && data!=null)

            {
                var recognizedSpeech: ArrayList<String>? = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                if(recognizedSpeech!=null)
                {
                    search.setText(recognizedSpeech.get(0))
                }

            }
        }
    }

}