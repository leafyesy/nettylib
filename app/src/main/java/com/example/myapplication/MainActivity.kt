package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.extlib.onClick
import com.example.myapplication.netty.SimpleNettyClientAdapter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val nettyClientAdapter by lazy { SimpleNettyClientAdapter() }

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
            nettyClientAdapter.send("这是测试数据 哈哈哈")
        }
    }

    private fun startTcpService() {
        nettyClientAdapter.connect()
        //nettyHelper.bind()
    }
}
