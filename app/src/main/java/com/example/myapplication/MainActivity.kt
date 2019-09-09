package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.extlib.onClick
import com.example.nettylib.NettyClientHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val nettyHelper by lazy { NettyClientHelper(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        server_btn.onClick { startTcpService() }
    }

    private fun startTcpService() {
        nettyHelper.bind()
    }
}
