package com.yrzapps.ytfy.adapters

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.yrzapps.ytfy.R


/* class SearchResultAdapter(val context : Activity, val info : List<Map<String,String>>) : BaseAdapter() {

    override fun getCount(): Int {
        return info.size
    }

    override fun getItem(p0: Int): Any {
        return info[p0]
    }

    override fun getItemId(p0: Int): Long {
        return 0L
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val resultView = p1?:context.layoutInflater.inflate(R.layout.custom_search_result,null)

        val ytInfo : Map<String,String> = info[p0]
        println(ytInfo)

        val thumbnail = resultView.findViewById<ImageView>(R.id.thumbnail)
        val channelThumbnail = resultView.findViewById<ShapeableImageView>(R.id.channelThumbnail)

        val title = resultView.findViewById<TextView>(R.id.videoTitle)
        val metaData = resultView.findViewById<TextView>(R.id.videoInfo)

        title.text = ytInfo["title"]
        metaData.text = "${ytInfo["channel"]} . ${ytInfo["views"]} . ${ytInfo["publish_time"]}"

        Glide.with(thumbnail).load(ytInfo["thumbnail"]).into(thumbnail)
        Glide.with(channelThumbnail).load(ytInfo["channel_thumbnail"]).into(channelThumbnail)

        return resultView
    }
} */


class SearchAdapter(val context : Activity,val info : List<Map<String,String>>) : RecyclerView.Adapter<SearchResultViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        return SearchResultViewHolder(context.layoutInflater.inflate(R.layout.custom_search_result,null))
    }

    override fun getItemCount(): Int {
        return info.size
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {

        val ytInfo : Map<String,String> = info[position]

        holder.title.text = ytInfo["title"]
        holder.metaData.text = "${ytInfo["channel"]} . ${ytInfo["views"]} . ${ytInfo["publish_time"]}"

        Glide.with(holder.thumbnail).load(ytInfo["thumbnail"]).into(holder.thumbnail)
        Glide.with(holder.channelThumbnail).load(ytInfo["channel_thumbnail"]).into(holder.channelThumbnail)
    }

    override fun registerAdapterDataObserver(observer: RecyclerView.AdapterDataObserver) {
        super.registerAdapterDataObserver(observer)
    }

}


data class SearchResultViewHolder(val resultView : View) : RecyclerView.ViewHolder(resultView)
{
    val thumbnail = resultView.findViewById<ImageView>(R.id.thumbnail)
    val channelThumbnail = resultView.findViewById<ShapeableImageView>(R.id.channelThumbnail)

    val title = resultView.findViewById<TextView>(R.id.videoTitle)
    val metaData = resultView.findViewById<TextView>(R.id.videoInfo)
}