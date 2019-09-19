package com.example.nettylib.udp

import com.google.protobuf.MessageLite
import io.netty.channel.ChannelHandlerContext

interface ProtoDataOperator {
    fun onReadData(channelHandlerContext: ChannelHandlerContext, messageLite: MessageLite)
}
