package com.example.nettylib.udp

import io.netty.buffer.Unpooled.wrappedBuffer
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.socket.DatagramPacket
import io.netty.handler.codec.MessageToMessageEncoder
import io.netty.util.internal.SocketUtils


class DataPacketProtoEncoder : MessageToMessageEncoder<ProtoDataPacket>() {
    @Throws(Exception::class)
    override fun encode(
        ctx: ChannelHandlerContext,
        msg: ProtoDataPacket,
        out: MutableList<Any>
    ) {
        out.add(
            DatagramPacket(
                wrappedBuffer(msg.data.toByteArray()),
                SocketUtils.socketAddress(msg.address, msg.port)
            )
        )
    }
}