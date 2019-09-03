package com.example.nettylib.tcp.server

import com.example.nettylib.proto.HeartBeatData
import com.google.protobuf.MessageLite
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler

class ServerDataHandler(private val serverChannelOperator: ServerChannelOperator?) :
    SimpleChannelInboundHandler<MessageLite>() {

    @Throws(Exception::class)
    override fun channelRead0(ctx: ChannelHandlerContext, msg: MessageLite) {
        if (msg !is HeartBeatData.HeartBeat) {
            //不是心跳
            serverChannelOperator?.onReadData(ctx, msg)
        } else {
            println("心跳")
        }
    }
}
