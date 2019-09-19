package com.example.nettylib.udp

import com.google.protobuf.MessageLite
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler

class ProtoDataHandler(private val protoDataOperator: ProtoDataOperator?) : SimpleChannelInboundHandler<MessageLite>() {

    @Throws(Exception::class)
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        //        super.exceptionCaught(ctx, cause);
        cause.printStackTrace()
    }

    @Throws(Exception::class)
    override fun channelRead0(ctx: ChannelHandlerContext, msg: MessageLite) {
        if (protoDataOperator != null) {
            protoDataOperator!!.onReadData(ctx, msg)
        }
    }
}