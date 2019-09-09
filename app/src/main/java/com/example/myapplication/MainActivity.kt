package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.extlib.onClick
import com.example.nettylib.Constant
import com.example.nettylib.NettyHelper
import com.example.nettylib.TcpMessageHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val nettyHelper by lazy { NettyHelper() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        server_btn.onClick { startTcpService() }
    }

    private fun startTcpService() {
        nettyHelper.bind()
    }
}
