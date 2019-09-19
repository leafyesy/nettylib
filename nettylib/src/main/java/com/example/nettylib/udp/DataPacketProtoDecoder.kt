package com.example.nettylib.udp

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.socket.DatagramPacket
import io.netty.handler.codec.MessageToMessageDecoder

class DataPacketProtoDecoder : MessageToMessageDecoder<DatagramPacket>() {
    @Throws(Exception::class)
    override fun decode(ctx: ChannelHandlerContext, msg: DatagramPacket, out: MutableList<Any>) {
        println(msg.sender().hostName)
        val data = msg.content()
        data.retain()
        out.add(data)
    }
}