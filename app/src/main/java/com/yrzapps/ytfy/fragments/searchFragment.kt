package com.yrzapps.ytfy.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.OnClickListener
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chaquo.python.Python
import com.yrzapps.ytfy.R
import com.yrzapps.ytfy.adapters.SearchAdapter
import com.yrzapps.ytfy.adapters.SuggestionsAdapter
import com.yrzapps.ytfy.core.YTInfoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class SearchFragment : Fragment(), OnClickListener, OnQueryTextListener {

    val main = Python.getInstance().getModule("main")

    lateinit var recycleView: RecyclerView
    lateinit var queryView: SearchView
    lateinit var suggestionsAdapter: SuggestionsAdapter
    lateinit var searchAdapter: SearchAdapter
    lateinit var loading : ImageView

    lateinit var miniPlayer : RelativeLayout

    var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        recycleView = view.findViewById<RecyclerView>(R.id.video_data)
        recycleView.layoutManager = LinearLayoutManager(requireContext())

        loading = view.findViewById(R.id.loading)
        Glide.with(loading).load(R.drawable.loading).into(loading)

        miniPlayer = requireActivity().findViewById<RelativeLayout>(R.id.miniPlayer)

        searchAdapter = SearchAdapter(requireActivity(), mutableListOf()) { _, info ->

            val infoModel = ViewModelProvider(requireActivity())[YTInfoViewModel::class.java]
            infoModel.info.value = info
            miniPlayer.visibility = VISIBLE

        }
        // recycleView.adapter = searchAdapter

        queryView = view.findViewById<SearchView>(R.id.search)
        queryView.setOnClickListener(this)
        queryView.setOnQueryTextListener(this)

        suggestionsAdapter = SuggestionsAdapter(requireContext(), mutableListOf()) { position ->
            val query: String = suggestionsAdapter.suggestions[position]

            queryView.setQuery(query, true)
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {

        loading.visibility = VISIBLE
        recycleView.visibility = GONE
        miniPlayer.visibility = GONE

        recycleView.adapter = searchAdapter
        queryView.isIconified = true
        queryView.clearFocus()
        queryView.clearAnimation()

        CoroutineScope(Dispatchers.IO).launch {
            val info: List<Map<String, String>> = main.callAttr("search", query).asList().map {
                it.asMap().entries.associate { (key, value) ->
                    if (value != null) key.toString() to value.toString() else key.toString() to ""
                }
            }

            CoroutineScope(Dispatchers.Main).launch {
                loading.visibility = GONE
                recycleView.visibility = VISIBLE
               // miniPlayer.visibility = VISIBLE

                searchAdapter.info.clear()
                searchAdapter.info.addAll(info)
                searchAdapter.notifyDataSetChanged()
            }

        }

        return false
    }

    override fun onQueryTextChange(query: String?): Boolean {

        searchJob?.cancel()

        searchJob = CoroutineScope(Dispatchers.IO).launch {

            val suggestionsObj = main.callAttr("getSuggestions", query!!) ?: return@launch
            val suggestions = suggestionsObj.asList().map { it.toString() }.toMutableList()

            if (suggestions.isEmpty()) return@launch

            searchJob = CoroutineScope(Dispatchers.Main).launch {

                suggestionsAdapter.suggestions.clear()
                suggestionsAdapter.suggestions.addAll(suggestions)
                suggestionsAdapter.notifyDataSetChanged()

            }
        }

        return true
    }

    override fun onClick(p0: View?) {

        when (p0!!.id) {
            queryView.id -> {
                recycleView.adapter = suggestionsAdapter
                queryView.isIconified = false
                queryView.requestFocus()
            }
        }

    }
}