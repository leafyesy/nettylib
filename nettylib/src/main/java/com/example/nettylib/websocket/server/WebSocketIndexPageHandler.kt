package com.example.nettylib.websocket.server

import io.netty.buffer.ByteBufUtil
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPipeline
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.*
import io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE
import io.netty.handler.codec.http.HttpMethod.GET
import io.netty.handler.codec.http.HttpResponseStatus.*
import io.netty.handler.ssl.SslHandler


/**
 * Created by leafye on 2019-11-28.
 */
class WebSocketIndexPageHandler(private val websocketPath: String) :
    SimpleChannelInboundHandler<FullHttpRequest>() {

    @Throws(Exception::class)
    override fun channelRead0(ctx: ChannelHandlerContext, req: FullHttpRequest) {
        print("channelRead0:$req")
        // Handle a bad request.
        if (!req.decoderResult().isSuccess) {
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

        // Send the index page
        if ("/" == req.uri() || "/index.html" == req.uri()) {
            val webSocketLocation = getWebSocketLocation(ctx.pipeline(), req, websocketPath)
            val content = WebSocketServerIndexPage.getContent(webSocketLocation)
            val res = DefaultFullHttpResponse(req.protocolVersion(), OK, content)

            res.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8")
            HttpUtil.setContentLength(res, content.readableBytes().toLong())

            sendHttpResponse(ctx, req, res)
        } else {
            sendHttpResponse(
                ctx, req, DefaultFullHttpResponse(
                    req.protocolVersion(), NOT_FOUND,
                    ctx.alloc().buffer(0)
                )
            )
        }
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        ctx.close()
    }

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
        val future = ctx.writeAndFlush(res)
        if (!keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE)
        }
    }

    private fun getWebSocketLocation(cp: ChannelPipeline, req: HttpRequest, path: String): String {
        var protocol = "ws"
        if (cp.get(SslHandler::class.java) != null) {
            // SSL in use so use Secure WebSockets
            protocol = "wss"
        }
        return protocol + "://" + req.headers().get(HttpHeaderNames.HOST) + path
    }
}
