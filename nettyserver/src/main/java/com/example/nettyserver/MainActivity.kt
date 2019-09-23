package com.example.nettyserver

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.extlib.onClick
import com.example.nettyserver.netty.SimpleNettyServerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    private val nettyAdapter by lazy {
        SimpleNettyServerAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        click_to_start_server.onClick { startServer() }
        click_to_send_data.onClick { send() }
    }

    private fun send() {
        nettyAdapter.send("this is server test data!!!")
    }

    private fun startServer() {
        //nettyHelper.bind()
        nettyAdapter.connect()
    }
}
