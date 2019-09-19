package com.example.nettyserver

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.extlib.onClick
import com.example.nettylib.NettyServerHelper
import com.example.nettylib.simple.TcpServer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val nettyHelper by lazy { NettyServerHelper(this) }

    private val tcpServer by lazy {
        TcpServer()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        click_to_start_server.onClick { startServer() }
        click_to_send_data.onClick { send() }
    }

    private fun send() {
        tcpServer.send("this is server test data!!!")
    }

    private fun startServer() {
        //nettyHelper.bind()
        tcpServer.connect()
    }
}
