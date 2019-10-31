package com.example.nettyserver

import android.content.Context
import androidx.multidex.MultiDexApplication

class MyApplication : MultiDexApplication() {

    companion object {
        private var context: Context? = null

        fun getContext() = context!!
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }



}