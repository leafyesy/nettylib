package com.example.nettylib.tcp.client

import com.example.nettylib.proto.HeartBeatData
import com.google.protobuf.MessageLite
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler

class ClientDataHandler(private val clientChannelOperator: ClientChannelOperator?) :
    SimpleChannelInboundHandler<MessageLite>() {

    @Throws(Exception::class)
    override fun channelRead0(ctx: ChannelHandlerContext, msg: MessageLite) {
        if (msg !is HeartBeatData.HeartBeat) {
            //不是心跳
            clientChannelOperator?.onReadData(ctx, msg)
        } else {
            println("心跳")
        }
    }
}
