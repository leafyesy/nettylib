package com.example.nettylib.tcp.client

import com.google.protobuf.MessageLite
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPipeline
import io.netty.util.concurrent.Future

abstract class ClientChannelOperator {
    abstract fun hasConnect(channelHandlerContext: ChannelHandlerContext)
    abstract fun hasDisconnect(channelHandlerContext: ChannelHandlerContext)
    abstract fun onReadData(channelHandlerContext: ChannelHandlerContext, data: MessageLite)
    abstract fun isConnect(): Boolean

    @Throws(Exception::class)
    abstract fun send(messageLite: MessageLite): Future<*>?

    @Throws(Exception::class)
    abstract fun send(byteBuf: ByteBuf): Future<*>?

    open fun handleChannelPipeline(channelPipeline: ChannelPipeline) {}
}