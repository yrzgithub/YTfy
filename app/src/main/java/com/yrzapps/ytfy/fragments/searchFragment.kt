package com.yrzapps.ytfy.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chaquo.python.Python
import com.yrzapps.ytfy.R
import com.yrzapps.ytfy.adapters.SearchAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


class SearchFragment : Fragment() {

    val main = Python.getInstance().getModule("main")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // val listView = view.findViewById<ListView>(R.id.video_data)

        val recycleView = view.findViewById<RecyclerView>(R.id.video_data)
        recycleView.layoutManager = LinearLayoutManager(requireContext())

        CoroutineScope(Dispatchers.IO).launch {

            val info : List<Map<String,String>> = main.callAttr("search","elengathu veesuthey").asList().map {
                it.asMap().entries.associate {
                        (key,value) -> if(value!=null) key.toString() to value.toString() else key.toString() to ""
                }
            }

            CoroutineScope(Dispatchers.Main).launch {
                recycleView.adapter = SearchAdapter(requireActivity(),info)
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }
}