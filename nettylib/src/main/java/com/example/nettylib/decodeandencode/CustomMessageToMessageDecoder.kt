package com.example.nettylib.decodeandencode

import android.util.Log
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageDecoder
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame
import io.netty.handler.codec.http.websocketx.WebSocketFrame

/**
 * Created by leafye on 2019-12-02.
 */
class CustomMessageToMessageDecoder : MessageToMessageDecoder<WebSocketFrame>() {

    private val TAG = "MessageToMessageDecoder"

    override fun decode(
        p0: ChannelHandlerContext?,
        frame: WebSocketFrame?,
        objs: MutableList<Any>?
    ) {
        Log.d(TAG, "进入Decoder frame:$frame")
        if (frame is BinaryWebSocketFrame){
            val buf = frame.content()
            objs?.add(buf)
            buf.retain()
        }
    }

}