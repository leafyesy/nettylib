package com.example.nettyserver.netty

/**
 * Created by leafye on 2019-09-21.
 */
interface INettyServerAdapter {

    fun connect()

    fun send(msg:String)

}