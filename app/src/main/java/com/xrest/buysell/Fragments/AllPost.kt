package com.xrest.buysell.Fragments

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.hardware.*
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.textfield.TextInputEditText
import com.xrest.buysell.Adapters.MainProductAdapter
import com.xrest.buysell.R
import com.xrest.buysell.Retrofit.Product
import com.xrest.buysell.Retrofit.Repo.ProductRepo
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.*


class AllPost : Fragment(),SensorEventListener, View.OnClickListener {
lateinit var mic: ImageButton
lateinit var search:TextInputEditText
lateinit var swipe:SwipeRefreshLayout
lateinit var rv:RecyclerView
    var adapter = GroupAdapter<GroupieViewHolder>()

    var lst:MutableList<Product> =mutableListOf()
    var slst:MutableList<Product> =mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=  inflater.inflate(R.layout.fragment_all_post, container, false)
        mic = view.findViewById(R.id.mic)
        swipe = view.findViewById(R.id.swipe)
        search = view.findViewById(R.id.search)
         rv = view.findViewById(R.id.rv)
        rv.layoutManager =LinearLayoutManager(requireContext())
        loadData(view)
        swipe.setOnRefreshListener(){
loadData(view)
            swipe.isRefreshing=false
        }


        mic.setOnClickListener(this)
        search.addTextChangedListener(object:TextWatcher{
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
                    addToAdapter(slst)
                }
                else{
                    addToAdapter(lst)
                }

            }
        })



        var sensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        var sensor =sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
            sensorManager.registerListener(this,sensor,SensorManager.SENSOR_STATUS_ACCURACY_HIGH)



        return view
    }


    override fun onResume() {
        super.onResume()
        var actionBar =(requireActivity() as AppCompatActivity).supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar.setTitle("")
        actionBar.hide()

    }

    override fun onSensorChanged(event: SensorEvent?) {
        Toast.makeText(requireContext(), "${event!!.values.get(0)}", Toast.LENGTH_SHORT).show()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onClick(v: View?) {
        when(v?.id)
        {
            R.id.mic ->{

                var intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault())
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Please Speak Now")
                startActivityForResult(intent,5)

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==Activity.RESULT_OK&&data!=null)
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

    fun addToAdapter(lst:MutableList<Product>)
    {
        adapter.clear()
        for(data in lst)
        {
            adapter.add(MainProductAdapter(requireContext(),data))
        }
    }

    fun loadData(view:View){
        view.findViewById<ShimmerFrameLayout>(R.id.shimmer).isVisible=true

//        var dialog =Dialog(requireContext())
//        dialog.setContentView(R.layout.success_notification)
//        dialog.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
//        dialog.window!!.attributes!!.windowAnimations = R.style.DialogAnimation
//        dialog.show()
//        dialog.setCancelable(false)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                var response = ProductRepo().getPost()
                if(response.success==true)
                {
                    withContext(Main)
                    {
                        lst = response.data!!
                        addToAdapter(lst)
                        //dialog.cancel()
                        rv.adapter =adapter
                      view.findViewById<ShimmerFrameLayout>(R.id.shimmer).isVisible=false
                    }
                }
            }
            catch (ex:Exception)
            {

            }
        }
    }
}