package com.example.nettylib.udp

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.channel.socket.DatagramPacket

class DatagramPacketHandler(private val dataOperator: DataOperator?) : SimpleChannelInboundHandler<DatagramPacket>() {

    @Throws(Exception::class)
    override fun channelRead0(channelHandlerContext: ChannelHandlerContext, datagramPacket: DatagramPacket) {
        if (dataOperator != null) {
            dataOperator!!.onReadData(channelHandlerContext, datagramPacket)
        }
    }
}
