package com.yrzapps.ytfy.core

import com.chaquo.python.Python

class YTInfo(query: String) {

    val main = Python.getInstance().getModule("main")

    var title: String? = null
    var channel: String? = null
    var views: String? = null
    var published: String? = null
    var thumbnail: String? = null
    var channelThumbNail: String? = null

    init {
        val info: Map<String, String> =
            main.callAttr("search", query).entries.associate { (key, value) ->
                key.toString() to value.toString()
            }

        println(info)
    }

}