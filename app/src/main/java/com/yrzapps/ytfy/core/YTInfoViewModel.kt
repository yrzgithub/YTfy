package com.yrzapps.ytfy.core

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.media3.exoplayer.ExoPlayer

class YTInfoViewModel() :ViewModel()
{
    val info = MutableLiveData<Map<String,String>>()
}