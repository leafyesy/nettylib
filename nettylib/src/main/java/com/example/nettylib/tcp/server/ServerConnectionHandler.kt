package com.example.nettylib.tcp.server

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter

class ServerConnectionHandler(private val serverChannelOperator: ServerChannelOperator) :
    ChannelInboundHandlerAdapter() {

    @Throws(Exception::class)
    override fun channelActive(ctx: ChannelHandlerContext) {
        println("channelActive 链接到服务端")
        super.channelActive(ctx)
        serverChannelOperator.holdChildChannel(ctx)
    }

    @Throws(Exception::class)
    override fun channelInactive(ctx: ChannelHandlerContext) {
        println("channelInactive 断开链接到服务端")
        super.channelInactive(ctx)
        serverChannelOperator.releaseChildChannel(ctx)
    }

    @Throws(Exception::class)
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        ctx.close()
    }
}