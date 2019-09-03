package com.example.nettylib.tcp.client

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter

class ClientConnectionHandler(private val clientChannelOperator: ClientChannelOperator) :
    ChannelInboundHandlerAdapter() {

    @Throws(Exception::class)
    override fun channelActive(ctx: ChannelHandlerContext) {
        super.channelActive(ctx)
        clientChannelOperator.hasConnect(ctx)
    }

    @Throws(Exception::class)
    override fun channelInactive(ctx: ChannelHandlerContext) {
        super.channelInactive(ctx)
        clientChannelOperator.hasDisconnect(ctx)
    }

    @Throws(Exception::class)
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        ctx.close()
    }
}