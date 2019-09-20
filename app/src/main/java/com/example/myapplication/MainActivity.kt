package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.extlib.onClick
import com.example.nettylib.NettyClientHelper
import com.example.nettylib.simple.TcpClient
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val nettyHelper by lazy { NettyClientHelper(this) }

    private val tcpClient by lazy { TcpClient() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        server_btn.onClick { startTcpService() }
        send_test_data_btn.onClick { sendTestData() }
    }

    private fun sendTestData() {
//        for (i in 0..999) {
//            nettyHelper.send("这是测试数据 哈哈哈 <<>>")
//        }
        (0..9).forEach { _ ->
            tcpClient.send("这是测试数据 哈哈哈")
        }
    }

    private fun startTcpService() {
        tcpClient.connect()
        //nettyHelper.bind()
    }
}
