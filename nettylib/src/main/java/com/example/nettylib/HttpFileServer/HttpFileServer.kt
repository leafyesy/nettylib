package com.example.nettylib.HttpFileServer

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelInitializer
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.HttpRequestDecoder
import io.netty.handler.codec.http.HttpResponseEncoder
import io.netty.handler.stream.ChunkedWriteHandler
import io.netty.util.concurrent.DefaultEventExecutor
import io.netty.util.concurrent.DefaultEventExecutorGroup
import io.netty.util.concurrent.EventExecutorGroup
import io.netty.util.internal.logging.InternalLoggerFactory
import io.netty.util.internal.logging.JdkLoggerFactory

class HttpFileServer {

    companion object {
        init {
            InternalLoggerFactory.setDefaultFactory(JdkLoggerFactory.INSTANCE)
        }
    }

    private var bossGroup: EventLoopGroup? = null
    private var workerGroup: EventLoopGroup? = null
    private var eventExecutors: EventExecutorGroup? = null

    @Throws(Exception::class)
    fun bind(port: Int, requestTransfer: RequestTransfer): ChannelFuture {
        if (bossGroup != null)
            throw Exception("不能重复绑定！")
        return bind(port, requestTransfer, -1)
    }

    @Throws(Exception::class)
    fun bind(port: Int, requestTransfer: RequestTransfer, executorSize: Int): ChannelFuture {
        if (bossGroup != null)
            throw Exception("不能重复绑定！")

        if (executorSize != -1)
            eventExecutors = DefaultEventExecutorGroup(executorSize)
        bossGroup = NioEventLoopGroup()
        workerGroup = NioEventLoopGroup()
        val b = ServerBootstrap()
        b.group(bossGroup, workerGroup).channel(NioServerSocketChannel::class.java)
            .childHandler(object : ChannelInitializer<SocketChannel>() {
                @Throws(Exception::class)
                override fun initChannel(ch: SocketChannel) {
                    ch.pipeline().addLast("http-decoder", HttpRequestDecoder())
                    ch.pipeline().addLast("http-aggregator", HttpObjectAggregator(65536))
                    ch.pipeline().addLast("http-encoder", HttpResponseEncoder())
                    ch.pipeline().addLast("http-chunked", ChunkedWriteHandler())
                    if (eventExecutors != null)
                        ch.pipeline().addLast(
                            eventExecutors,
                            "fileServerHandler",
                            HttpFileServerHandler(requestTransfer)
                        )
                    else
                        ch.pipeline().addLast(
                            DefaultEventExecutor(),
                            "fileServerHandler",
                            HttpFileServerHandler(requestTransfer)
                        )
                }
            })

        return b.bind(port)

    }

    fun shutdown() {
        bossGroup?.shutdownGracefully()
        workerGroup?.shutdownGracefully()
        eventExecutors?.shutdownGracefully()
    }


}