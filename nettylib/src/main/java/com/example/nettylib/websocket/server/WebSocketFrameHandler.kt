package com.example.nettylib.websocket.server

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame
import io.netty.handler.codec.http.websocketx.WebSocketFrame
import java.util.*

/**
 * Created by leafye on 2019-11-28.
 */
class WebSocketFrameHandler : SimpleChannelInboundHandler<WebSocketFrame>() {

    //每个channel都有一个唯一的id值
    @Throws(Exception::class)
    override fun handlerAdded(ctx: ChannelHandlerContext?) {
        //打印出channel唯一值，asLongText方法是channel的id的全名
        println("handlerAdded：" + ctx?.channel()?.id()?.asLongText());
    }

    @Throws(Exception::class)
    override fun handlerRemoved(ctx: ChannelHandlerContext?) {
        println("handlerRemoved：" + ctx?.channel()?.id()?.asLongText());
    }

    @Throws(Exception::class)
    override fun channelRead0(ctx: ChannelHandlerContext, frame: WebSocketFrame) {
        // ping and pong frames already handled
        println("frame:$frame")
        if (frame is TextWebSocketFrame) {
            // Send the uppercase string back.
            val request = frame.text()
            ctx.channel().writeAndFlush(TextWebSocketFrame(request.toUpperCase(Locale.US)))
        } else {
            val message = "unsupported frame type: " + frame.javaClass.name
            throw UnsupportedOperationException(message)
        }
    }
}