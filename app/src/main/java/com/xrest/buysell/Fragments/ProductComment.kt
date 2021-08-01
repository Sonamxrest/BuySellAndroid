package com.xrest.buysell.Fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.xrest.buysell.Adapters.CommentAdapter
import com.xrest.buysell.R
import com.xrest.buysell.Retrofit.Comment
import com.xrest.buysell.Retrofit.Person
import com.xrest.buysell.Retrofit.Repo.ProductRepo
import com.xrest.buysell.Retrofit.RetroftiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception


class ProductComment(var id:String,var lst:MutableList<Comment>) : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        var view=  inflater.inflate(R.layout.fragment_product_comment, container, false)
        val rv: RecyclerView = view.findViewById(R.id.rv)
        val fb: FloatingActionButton = view.findViewById(R.id.addCommnet)
        var adapter = CommentAdapter(requireContext(),lst,id)
        fb.setOnClickListener(){
            var dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.update_edittet)
            dialog.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            var button: Button = dialog.findViewById(R.id.comment)
            var edt: TextInputEditText = dialog.findViewById(R.id.edtComment)

            button.setOnClickListener(){
                var person = RetroftiService.users
                try{
                    CoroutineScope(Dispatchers.IO).launch {
                        val reposen = ProductRepo().comment(Comment(_id = id,comment = edt.text.toString()))
                        if(reposen.success==true)
                        {
                            withContext(Dispatchers.Main)
                            {
                                lst.add(
                                    Comment(user= Person(_id= person!!._id,Name=person.Name,Username = person.Username,Profile = person.Profile),comment = edt.text.toString())
                                )
                                adapter= CommentAdapter(requireContext(),lst,id)
                                adapter.notifyDataSetChanged()
                                rv.adapter=adapter
                                dialog.cancel()

                            }



                        }

                    }
                }
                catch (ex: Exception){

                }
            }



            dialog.setCancelable(true)
            dialog.show()
        }
        rv.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        if(lst.size>0)
        {

            rv.adapter =adapter
        }
        return view
    }


}