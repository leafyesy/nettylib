package com.example.nettyserver.netty

import com.example.nettylib.NettyServerHelper
import com.example.nettyserver.MyApplication

/**
 * Created by leafye on 2019-09-21.
 */
class SimpleNettyServerAdapter : INettyServerAdapter {

    val tcpProtobufServer: NettyServerHelper by lazy {
        NettyServerHelper(MyApplication.getContext())
    }


//    val tcpServer by lazy {
//        TcpServer()
//    }

    override fun connect() {
        //tcpServer.connect()
        tcpProtobufServer.bind()
    }


    override fun send(msg: String) {
//        tcpProtobufServer.send(msg)
        //tcpProtobufServer.shutdown()
    }

}