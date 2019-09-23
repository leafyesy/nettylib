package com.example.myapplication.netty

/**
 * Created by leafye on 2019-09-21.
 */
interface INettyClientAdapter {

    fun connect()

    fun send(msg:String)

}