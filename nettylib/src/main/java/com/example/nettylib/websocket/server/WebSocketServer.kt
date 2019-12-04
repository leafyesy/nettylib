package com.example.nettylib.websocket.server

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.ssl.SslContext
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.SelfSignedCertificate


/**
 * Created by leafye on 2019-11-29.
 */
object WebSocketServer {

    internal val SSL = System.getProperty("ssl") != null
    internal val PORT = Integer.parseInt(System.getProperty("port", if (SSL) "8443" else "8080")!!)

    @Throws(Exception::class)
    @JvmStatic
    fun main() {
        // Configure SSL.
        val sslCtx: SslContext?
        if (SSL) {
            val ssc = SelfSignedCertificate()
            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build()
        } else {
            sslCtx = null
        }

        val bossGroup = NioEventLoopGroup()
        val workerGroup = NioEventLoopGroup()
        try {
            val b = ServerBootstrap()
            b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel::class.java)
                //.handler(LoggingHandler(LogLevel.INFO))
                //.childHandler(WebSocketServerInitializer(sslCtx, null))

            val ch = b.bind(PORT).sync().channel()

            println(
                "Open your web browser and navigate to " +
                        (if (SSL) "https" else "http") + "://127.0.0.1:" + PORT + '/'.toString()
            )
            ch.closeFuture().sync()
        } finally {
            bossGroup.shutdownGracefully()
            workerGroup.shutdownGracefully()
        }
    }
}