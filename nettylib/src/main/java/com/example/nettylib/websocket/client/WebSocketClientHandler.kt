package com.example.nettylib.websocket.client

import io.netty.channel.*
import io.netty.handler.codec.http.DefaultHttpHeaders
import io.netty.handler.codec.http.FullHttpResponse
import io.netty.handler.codec.http.websocketx.*
import io.netty.util.CharsetUtil
import java.net.URI

/**
 * Created by leafye on 2019-11-27.
 */
@ChannelHandler.Sharable
class WebSocketClientHandler(val uri: URI) :
    SimpleChannelInboundHandler<Any>() {
    private var handshakeFuture: ChannelPromise? = null

    private var handshaker: WebSocketClientHandshaker? = null


    fun handshakeFuture(): ChannelFuture? {
        return handshakeFuture
    }

    override fun handlerAdded(ctx: ChannelHandlerContext) {
        handshakeFuture = ctx.newPromise()
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        handshaker = WebSocketClientHandshakerFactory.newHandshaker(
            uri,
            WebSocketVersion.V13,
            null,
            true,
            DefaultHttpHeaders()
        ).apply {
            println("WebSocket Client handshake!   ${ctx.channel()}")
            handshake(ctx.channel())
            println("WebSocket Client handshake!   end!!!")
        }

    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        println("WebSocket Client disconnected!")
    }

    @Throws(Exception::class)
    override fun channelRead0(ctx: ChannelHandlerContext, msg: Any) {
        val ch = ctx.channel()
        val h = handshaker?:return
        if (!h.isHandshakeComplete) {
            try {
                h.finishHandshake(ch, msg as FullHttpResponse)
                println("WebSocket Client connected!")
                handshakeFuture!!.setSuccess()
            } catch (e: WebSocketHandshakeException) {
                println("WebSocket Client failed to connect")
                handshakeFuture!!.setFailure(e)
            }
            return
        }

        if (msg is FullHttpResponse) {
            val response = msg
            throw IllegalStateException(
                "Unexpected FullHttpResponse (getStatus=" + response.status() +
                        ", content=" + response.content().toString(CharsetUtil.UTF_8) + ')'.toString()
            )
        }

        val frame = msg as WebSocketFrame
        when (frame) {
            is TextWebSocketFrame -> {
                val textFrame = frame
                println("WebSocket Client received message: " + textFrame.text())
            }
            is PongWebSocketFrame -> println("WebSocket Client received pong")
            is CloseWebSocketFrame -> {
                println("WebSocket Client received closing")
                ch.close()
            }
        }
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        if (!handshakeFuture!!.isDone) {
            handshakeFuture!!.setFailure(cause)
        }
        ctx.close()
    }
}