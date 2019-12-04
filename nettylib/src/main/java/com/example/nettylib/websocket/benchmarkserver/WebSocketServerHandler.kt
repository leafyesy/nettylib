package com.example.nettylib.websocket.benchmarkserver

import android.util.Log
import com.example.nettylib.websocket.server.WebSocketServer
import com.google.protobuf.MessageLite
import io.netty.buffer.ByteBufUtil
import io.netty.buffer.UnpooledHeapByteBuf
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.*
import io.netty.handler.codec.http.HttpMethod.GET
import io.netty.handler.codec.http.HttpResponseStatus.*
import io.netty.handler.codec.http.websocketx.*


/**
 * Created by leafye on 2019-12-01.
 */
class WebSocketServerHandler(private val msgLite: MessageLite) :
    SimpleChannelInboundHandler<Any>() {

    private val TAG = "WebSocketServerHandler"

    private var handshaker: WebSocketServerHandshaker? = null

    override fun channelRead0(ctx: ChannelHandlerContext, msg: Any) {
        Log.d(TAG, "channelRead0:$msg")
        if (msg is FullHttpRequest) {
            handleHttpRequest(ctx, msg)
        } else if (msg is WebSocketFrame) {
            handleWebSocketFrame(ctx, msg)
        } else if (msg is UnpooledHeapByteBuf) {
            val copy = msg.copy().array()
            Log.d(TAG, "copy:$copy")
            val parseFrom = msgLite.parserForType.parseFrom(copy)
            Log.d(TAG, "parseFrom:$parseFrom")
        }
    }

    override fun channelReadComplete(ctx: ChannelHandlerContext) {
        Log.d(TAG, "channelReadComplete")
        ctx.flush()
    }

    private fun handleHttpRequest(ctx: ChannelHandlerContext, req: FullHttpRequest) {
        // Handle a bad request.
        if (!req.decoderResult().isSuccess()) {
            sendHttpResponse(
                ctx, req, DefaultFullHttpResponse(
                    req.protocolVersion(), BAD_REQUEST,
                    ctx.alloc().buffer(0)
                )
            )
            return
        }

        // Allow only GET methods.
        if (!GET.equals(req.method())) {
            sendHttpResponse(
                ctx, req, DefaultFullHttpResponse(
                    req.protocolVersion(), FORBIDDEN,
                    ctx.alloc().buffer(0)
                )
            )
            return
        }

        // Send the demo page and favicon.ico
        if ("/" == req.uri()) {
            val content = WebSocketServerBenchmarkPage.getContent(getWebSocketLocation(req))
            val res = DefaultFullHttpResponse(req.protocolVersion(), OK, content)

            res.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8")
            HttpUtil.setContentLength(res, content.readableBytes().toLong())

            sendHttpResponse(ctx, req, res)
            return
        }

        if ("/favicon.ico" == req.uri()) {
            val res = DefaultFullHttpResponse(
                req.protocolVersion(), NOT_FOUND,
                ctx.alloc().buffer(0)
            )
            sendHttpResponse(ctx, req, res)
            return
        }

        // Handshake
        val wsFactory = WebSocketServerHandshakerFactory(
            getWebSocketLocation(req), null, true, 5 * 1024 * 1024
        )
        handshaker = wsFactory.newHandshaker(req)
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel())
        } else {
            handshaker!!.handshake(ctx.channel(), req)
        }
    }

    private fun handleWebSocketFrame(ctx: ChannelHandlerContext, frame: WebSocketFrame) {
        Log.d(TAG, "进入handleWebSocketFrame")
        // Check for closing frame
        if (frame is CloseWebSocketFrame) {
            handshaker!!.close(ctx.channel(), frame.retain() as CloseWebSocketFrame)
            return
        }
        if (frame is PingWebSocketFrame) {
            ctx.write(PongWebSocketFrame(frame.content().retain()))
            return
        }
        if (frame is TextWebSocketFrame) {
            // Echo the frame
            ctx.write(frame.retain())
            return
        }
        if (frame is BinaryWebSocketFrame) {
            Log.d(TAG, "进入BinaryWebSocketFrame")
            // Echo the frame
            ctx.write(frame.retain())
        }
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        ctx.close()
    }

    companion object {

        private val WEBSOCKET_PATH = "/websocket"

        private fun sendHttpResponse(
            ctx: ChannelHandlerContext,
            req: FullHttpRequest,
            res: FullHttpResponse
        ) {
            // Generate an error page if response getStatus code is not OK (200).
            val responseStatus = res.status()
            if (responseStatus.code() !== 200) {
                ByteBufUtil.writeUtf8(res.content(), responseStatus.toString())
                HttpUtil.setContentLength(res, res.content().readableBytes().toLong())
            }
            // Send the response and close the connection if necessary.
            val keepAlive = HttpUtil.isKeepAlive(req) && responseStatus.code() === 200
            HttpUtil.setKeepAlive(res, keepAlive)
            val future = ctx.write(res) // Flushed in channelReadComplete()
            if (!keepAlive) {
                future.addListener(ChannelFutureListener.CLOSE)
            }
        }

        private fun getWebSocketLocation(req: FullHttpRequest): String {
            val location = req.headers().get(HttpHeaderNames.HOST) + WEBSOCKET_PATH
            return if (WebSocketServer.SSL) {
                "wss://$location"
            } else {
                "ws://$location"
            }
        }
    }
}
