package com.yrzapps.ytfy.fragments

import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Player.Listener
import com.bumptech.glide.Glide
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.yrzapps.ytfy.R
import com.yrzapps.ytfy.core.YTInfoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit


class PlayerFragment : Fragment(), OnClickListener,OnSeekBarChangeListener,Listener {

    lateinit var player : ExoPlayer
    lateinit var main : PyObject
    lateinit var currentPosition : TextView
    lateinit var seek : SeekBar
    lateinit var play : ImageButton
    lateinit var endPosition : TextView

    var runnable : Runnable ?= null
    val handler = Handler(Looper.getMainLooper())

    companion object {
        val seekDuration : Int = 5000
    }

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
        currentPosition = view.findViewById<TextView>(R.id.currentPosition)
        endPosition = view.findViewById<TextView>(R.id.endPosition)
        play = view.findViewById<ImageButton>(R.id.play_pause)
        val backward = view.findViewById<ImageView>(R.id.backward)
        val forward = view.findViewById<ImageView>(R.id.forward)
        seek = view.findViewById<SeekBar>(R.id.seekBar)

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
                val streamUrl = main.callAttr("getStream",url).toString()

                println(streamUrl)

                CoroutineScope(Dispatchers.Main).launch {

                    val media = MediaItem.fromUri(streamUrl)
                    player.setMediaItem(media)
                    player.prepare()
                    player.play()
                }

            }
        }

        seek.setOnSeekBarChangeListener(this)
        play.setOnClickListener(this)
        forward.setOnClickListener(this)
        backward.setOnClickListener(this)
        player.addListener(this)

        runnable = object : Runnable
        {
            override fun run() {
                val position = player.currentPosition
                seek.progress = position.toInt()

                val min = TimeUnit.MILLISECONDS.toMinutes(position)
                val sec = TimeUnit.MILLISECONDS.toSeconds(position) % 60
                val currentTime = String.format("%d:%02d",min,sec)

                currentPosition.text = currentTime

                seek.secondaryProgress = player.bufferedPosition.toInt()

                println(currentTime)

                handler.postDelayed(this,1000)
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onPlaybackStateChanged(playbackState: Int) {

        when(playbackState)
        {
            ExoPlayer.STATE_READY -> {
                seek.max = player.duration.toInt()
                handler.postDelayed(runnable!!, 1000)
            }

            ExoPlayer.STATE_ENDED -> {
                currentPosition.text = endPosition.text
                handler.removeCallbacksAndMessages(null)
            }
        }

        super.onPlaybackStateChanged(playbackState)
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        play.setImageResource(if(isPlaying) R.drawable.pause else R.drawable.play_arrow)
        super.onIsPlayingChanged(isPlaying)
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {

            R.id.play_pause -> if(player.isPlaying) player.pause() else player.play()
            R.id.forward -> player.seekTo(player.currentPosition + seekDuration)
            R.id.backward -> player.seekTo(player.currentPosition - seekDuration)

        }
    }

    override fun onProgressChanged(p0: SeekBar?, position: Int, byUser: Boolean) {
        if (byUser) {
            player.seekTo(position.toLong())
            return
        }
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
        player.pause()
    }

    override fun onStopTrackingTouch(p0: SeekBar?) {
        player.play()
    }
}