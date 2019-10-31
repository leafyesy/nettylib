package com.example.myapplication.netty

import com.example.myapplication.MyApplication
import com.example.nettylib.NettyClientHelper


/**
 * Created by leafye on 2019-09-21.
 */
class SimpleNettyClientAdapter : INettyClientAdapter {

    //private val tcpClient by lazy { TcpClient() }

    private val nettyClientHelper by lazy {
        NettyClientHelper(MyApplication.getContext())
    }

    override fun connect() {
        nettyClientHelper.bind()
        //tcpClient.connect()
    }

    override fun send(msg: String) {
        nettyClientHelper.shutdown()
        //tcpClient.send(msg)
    }

}