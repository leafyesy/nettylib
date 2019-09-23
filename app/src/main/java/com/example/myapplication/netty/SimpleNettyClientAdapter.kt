package com.example.myapplication.netty

import com.example.nettylib.simple.TcpClient

/**
 * Created by leafye on 2019-09-21.
 */
class SimpleNettyClientAdapter : INettyClientAdapter {

    private val tcpClient by lazy { TcpClient() }

    override fun connect() {
        tcpClient.connect()
    }

    override fun send(msg: String) {
        tcpClient.send(msg)
    }

}