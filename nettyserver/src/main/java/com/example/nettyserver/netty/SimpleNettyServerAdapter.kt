package com.example.nettyserver.netty

import com.example.nettylib.simple.TcpServer

/**
 * Created by leafye on 2019-09-21.
 */
class SimpleNettyServerAdapter:INettyServerAdapter{

    val tcpServer by lazy {
        TcpServer()
    }

    override fun connect(){
        tcpServer.connect()
    }


    override fun send(msg:String){
        tcpServer.send(msg)
    }

}