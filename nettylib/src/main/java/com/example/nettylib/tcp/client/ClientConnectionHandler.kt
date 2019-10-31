package com.example.nettylib.tcp.client

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter

class ClientConnectionHandler(private val tcpClientOperator: ITcpClientOperator) :
    ChannelInboundHandlerAdapter() {

    @Throws(Exception::class)
    override fun channelActive(ctx: ChannelHandlerContext) {
        super.channelActive(ctx)
        tcpClientOperator.channelActive(ctx)
        //tcpClientOperator.hasConnect(ctx)
    }

    @Throws(Exception::class)
    override fun channelInactive(ctx: ChannelHandlerContext) {
        super.channelInactive(ctx)
        tcpClientOperator.channelInactive(ctx)
        //clientChannelOperator.hasDisconnect(ctx)
    }

    /**
     * 捕捉到异常 关闭当前的链接
     */
    @Throws(Exception::class)
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        ctx.close()
    }
}