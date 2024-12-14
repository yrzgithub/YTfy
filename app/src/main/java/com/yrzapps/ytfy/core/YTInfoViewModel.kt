package com.yrzapps.ytfy.core

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class YTInfoViewModel : ViewModel() {
    val info = MutableLiveData<Map<String, String>>()
}