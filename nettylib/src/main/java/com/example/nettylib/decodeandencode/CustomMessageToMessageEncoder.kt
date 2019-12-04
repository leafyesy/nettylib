package com.example.nettylib.decodeandencode

import android.util.Log
import com.google.protobuf.MessageLite
import com.google.protobuf.MessageLiteOrBuilder
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled.wrappedBuffer
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageEncoder
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame


/**
 * Created by leafye on 2019-12-02.
 */
class CustomMessageToMessageEncoder : MessageToMessageEncoder<MessageLiteOrBuilder>() {
    private val TAG = "MessageToMessageEncoder"

    override fun encode(
        p0: ChannelHandlerContext?,
        msg: MessageLiteOrBuilder?,
        out: MutableList<Any>?
    ) {
        Log.d(TAG, "进入encode msg:$msg")
        var result: ByteBuf? = null
        if (msg is MessageLite) {
            result = wrappedBuffer(msg.toByteArray())
        }
        if (msg is MessageLite.Builder) {
            result = wrappedBuffer(msg.build().toByteArray())
        }
        // ==== 上面代码片段是拷贝自TCP ProtobufEncoder 源码 ====
        // 然后下面再转成websocket二进制流，因为客户端不能直接解析protobuf编码生成的
        val frame = BinaryWebSocketFrame(result)
        out?.add(frame)
    }

}