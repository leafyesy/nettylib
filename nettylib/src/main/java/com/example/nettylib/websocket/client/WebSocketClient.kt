package com.example.nettylib.websocket.client

import io.netty.bootstrap.Bootstrap
import io.netty.buffer.Unpooled
import io.netty.channel.Channel
import io.netty.channel.ChannelInitializer
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.http.HttpClientCodec
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler
import io.netty.handler.ssl.SslContext
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URI


/**
 * Created by leafye on 2019-11-29.
 */
object WebSocketClient {

    internal val URL = System.getProperty("url", "ws://127.0.0.1:8080/websocket")

    @Throws(Exception::class)
    @JvmStatic
    fun main(): Channel? {
        val ch: Channel?
        val uri = URI(URL)
        val scheme = if (uri.getScheme() == null) "ws" else uri.getScheme()
        val host = if (uri.getHost() == null) "127.0.0.1" else uri.getHost()
        val port: Int
        if (uri.getPort() === -1) {
            if ("ws".equals(scheme, ignoreCase = true)) {
                port = 80
            } else if ("wss".equals(scheme, ignoreCase = true)) {
                port = 443
            } else {
                port = -1
            }
        } else {
            port = uri.getPort()
        }

        if (!"ws".equals(scheme, ignoreCase = true) && !"wss".equals(scheme, ignoreCase = true)) {
            System.err.println("Only WS(S) is supported.")
            return null
        }

        val ssl = "wss".equals(scheme, ignoreCase = true)
        val sslCtx: SslContext?
        if (ssl) {
            sslCtx = SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE).build()
        } else {
            sslCtx = null
        }

        val group = NioEventLoopGroup()
        try {
            // Connect with V13 (RFC 6455 aka HyBi-17). You can change it to V08 or V00.
            // If you change it to V00, ping is not supported and remember to change
            // HttpResponseDecoder to WebSocketHttpResponseDecoder in the pipeline.
            val handler = WebSocketClientHandler(uri)
            val b = Bootstrap()
            b.group(group)
                .channel(NioSocketChannel::class.java)
                .handler(object : ChannelInitializer<SocketChannel>() {
                    protected override fun initChannel(ch: SocketChannel) {
                        val p = ch.pipeline()
                        if (sslCtx != null) {
                            p.addLast(sslCtx!!.newHandler(ch.alloc(), host, port))
                        }
                        p.addLast(
                            HttpClientCodec(),
                            HttpObjectAggregator(8192),
                            WebSocketClientCompressionHandler.INSTANCE,
                            handler
                        )
                    }
                })

            ch = b.connect(uri.getHost(), port).sync().channel()
            handler.handshakeFuture()!!.sync()
            val console = BufferedReader(InputStreamReader(System.`in`))
            while (true) {
                val msg = console.readLine()
                if (msg == null) {
                    break
                } else if ("bye" == msg!!.toLowerCase()) {
                    ch.writeAndFlush(CloseWebSocketFrame())
                    ch.closeFuture().sync()
                    break
                } else if ("ping" == msg!!.toLowerCase()) {
                    val frame = PingWebSocketFrame(Unpooled.wrappedBuffer(byteArrayOf(8, 1, 8, 1)))
                    ch.writeAndFlush(frame)
                } else {
                    val frame = TextWebSocketFrame(msg)
                    ch.writeAndFlush(frame)
                }
            }
            return ch
        } finally {
            group.shutdownGracefully()
        }
    }
}