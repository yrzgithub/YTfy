package com.yrzapps.ytfy.adapters

import android.app.Activity
import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.yrzapps.ytfy.R


class SearchAdapter(
    val context: Activity,
    val info: MutableList<Map<String, String>>,
    val onClick: (Int, Map<String, String>) -> Unit
) : RecyclerView.Adapter<SearchResultViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        return SearchResultViewHolder(
            context.layoutInflater.inflate(
                R.layout.custom_search_result,
                null
            )
        )
    }

    override fun getItemCount(): Int {
        return info.size
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {

        holder.resultView.setOnClickListener {
            onClick(position, info[position])
        }

        val ytInfo: Map<String, String> = info[position]

        holder.title.text = ytInfo["title"]
        holder.metaData.text =
            "${ytInfo["channel"]} . ${ytInfo["views"]} . ${ytInfo["publish_time"]}"

        Glide.with(holder.thumbnail).load(ytInfo["thumbnail"]).into(holder.thumbnail)
        Glide.with(holder.channelThumbnail).load(ytInfo["channel_thumbnail"])
            .into(holder.channelThumbnail)
    }

}


data class SearchResultViewHolder(val resultView: View) : RecyclerView.ViewHolder(resultView) {
    val thumbnail = resultView.findViewById<ImageView>(R.id.thumbnail)
    val channelThumbnail = resultView.findViewById<ShapeableImageView>(R.id.channelThumbnail)

    val title = resultView.findViewById<TextView>(R.id.videoTitle)
    val metaData = resultView.findViewById<TextView>(R.id.videoInfo)
}


class SuggestionsAdapter(
    val context: Context,
    val suggestions: MutableList<String>,
    val onClick: (Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var suggest: TextView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        suggest = TextView(context)
        suggest.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18F)
        suggest.setPadding(15, 20, 0, 20)

        return object : RecyclerView.ViewHolder(suggest) {

        }
    }

    override fun getItemCount(): Int {
        return suggestions.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        suggest.setOnClickListener {
            onClick(position)
        }
        suggest.text = suggestions[position]
    }

}