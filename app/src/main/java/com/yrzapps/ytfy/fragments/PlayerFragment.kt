package com.yrzapps.ytfy.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.SeekParameters
import com.bumptech.glide.Glide
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.yrzapps.ytfy.R
import com.yrzapps.ytfy.core.YTInfoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class PlayerFragment : Fragment(), OnClickListener {

    lateinit var player : ExoPlayer
    lateinit var main : PyObject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val thumbnail = view.findViewById<ImageView>(R.id.thumbnail)
        val title = view.findViewById<TextView>(R.id.title)
        val channelName = view.findViewById<TextView>(R.id.channel)
        val currentPosition = view.findViewById<TextView>(R.id.currentPosition)
        val endPosition = view.findViewById<TextView>(R.id.endPosition)
        val play = view.findViewById<ImageButton>(R.id.play_pause)
        val backward = view.findViewById<ImageView>(R.id.backward)
        val forward = view.findViewById<ImageView>(R.id.forward)

        player = ExoPlayer.Builder(requireContext()).build()
        main = Python.getInstance().getModule("main")

        val infoModel = ViewModelProvider(requireActivity())[YTInfoViewModel::class.java]
        infoModel.info.observe(viewLifecycleOwner) {
            info ->
            Glide.with(thumbnail).load(info["thumbnail"]).into(thumbnail)

            title.text = info["title"]
            channelName.text = info["channel"]
            endPosition.text = info["duration"]

            CoroutineScope(Dispatchers.IO).launch {
                val url = "https://www.youtube.com/watch?v=${info["id"]}"
                val stream_url = main.callAttr("getStream",url).toString()

                println(stream_url)

                CoroutineScope(Dispatchers.Main).launch {
                    val media = MediaItem.fromUri(stream_url)
                    player.setMediaItem(media)
                    player.prepare()
                    player.play()
                }

            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {

            R.id.play_pause -> if(player.isPlaying) player.pause() else player.pause()
            R.id.forward -> player.seekForward()

        }
    }
}