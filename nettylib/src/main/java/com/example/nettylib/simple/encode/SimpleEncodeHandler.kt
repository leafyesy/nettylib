package com.example.nettylib.simple.encode

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder

/**
 * Created by leafye on 2019-09-20.
 */
class SimpleEncodeHandler : MessageToByteEncoder<String>() {
    override fun encode(p0: ChannelHandlerContext?, p1: String?, p2: ByteBuf?) {
        p0 ?: return
        p1 ?: return
        p2 ?: return
        //进行数据encode
        val byteArray = p1.toByteArray()
        p2.writeBytes(byteArray)

    }
}