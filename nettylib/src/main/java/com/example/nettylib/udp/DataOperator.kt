package com.example.nettylib.udp

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.socket.DatagramPacket

interface DataOperator {
    fun onReadData(channelHandlerContext: ChannelHandlerContext, msg: DatagramPacket)
}