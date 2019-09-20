package com.example.nettylib.simple.decode

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder

/**
 * Created by leafye on 2019-09-20.
 */
class SimpleDecodeHandler : ByteToMessageDecoder() {
    override fun decode(p0: ChannelHandlerContext?, p1: ByteBuf?, p2: MutableList<Any>?) {
        p0 ?: return
        p1 ?: return
        p2 ?: return
        val readableBytes = p1.readableBytes()
        val size = p1.capacity()
        val byteArray = ByteArray(readableBytes)
        p1.readBytes(byteArray)
        val string = String(byteArray)
        p2.add(string)

    }

}