package com.example.nettylib.tcp.server

import com.google.protobuf.MessageLite
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.util.internal.logging.InternalLoggerFactory
import io.netty.util.internal.logging.JdkLoggerFactory

class TcpProtobufServer : ServerChannelOperator() {
    private var bossGroup: EventLoopGroup? = null
    private var workerGroup: EventLoopGroup? = null
    var prototype: MessageLite? = null
        private set
    var port: Int = 0
        private set

    init {
        InternalLoggerFactory.setDefaultFactory(JdkLoggerFactory.INSTANCE)
    }

    fun bind(port: Int, prototype: MessageLite?): ChannelFuture {
        if (prototype == null)
            throw RuntimeException("请传入prototype")

        this.prototype = prototype
        this.port = port

        if (bossGroup != null || workerGroup != null) {
            throw RuntimeException("不能重复绑定")
        }

        // 配置服务端的NIO线程组
        bossGroup = NioEventLoopGroup()
        workerGroup = NioEventLoopGroup()

        val b = ServerBootstrap()
        b.group(bossGroup!!, workerGroup!!)
            .channel(NioServerSocketChannel::class.java)
            .childOption(ChannelOption.SO_KEEPALIVE, true)
            .childHandler(ServerChannelInitializer(prototype, this))

        // 绑定端口，同步等待成功
        return b.bind(port)
    }

    /**
     * 关闭
     */
    @Throws(Exception::class)
    fun shutdown() {
        if (bossGroup == null || workerGroup == null) {
            throw Exception("还未绑定！")
        }

        bossGroup!!.shutdownGracefully()
        workerGroup!!.shutdownGracefully()
        bossGroup = null
        workerGroup = null
    }

    override fun onReadData(channelHandlerContext: ChannelHandlerContext, messageLite: MessageLite) {

    }
}