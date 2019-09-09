package com.example.nettyserver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.extlib.onClick
import com.example.nettylib.NettyServerHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val nettyHelper by lazy { NettyServerHelper(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        click_to_start_server.onClick { startServer() }
    }

    private fun startServer() {
        nettyHelper.bind()
    }
}
