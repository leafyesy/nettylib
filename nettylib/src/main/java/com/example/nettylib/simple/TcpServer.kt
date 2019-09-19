package com.example.nettylib.simple

import android.text.TextUtils
import android.util.Log
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel

/**
 * Created by leafye on 2019-09-19.
 */
class TcpServer {

    companion object {
        private val TAG: String = TcpServer::class.java.simpleName
    }

    private val boosGroup by lazy { NioEventLoopGroup() }
    private val workerGroup by lazy { NioEventLoopGroup() }

    private var ctx: ChannelHandlerContext? = null

    private val serverBootStrap by lazy {
        ServerBootstrap().apply {
            //设置线程池
            group(boosGroup, workerGroup)
            //设置socket工厂
            channel(NioServerSocketChannel::class.java)
            // 超时时间
            option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            //保持链接
            childOption(ChannelOption.SO_KEEPALIVE, true)
            //tcp不延迟发送
            childOption(ChannelOption.TCP_NODELAY, true)
            //设置管道工厂
            childHandler(object : ChannelInitializer<SocketChannel>() {
                override fun initChannel(p0: SocketChannel?) {
                    p0 ?: return
                    Log.d(TAG, "服务端接受到channel处理数据")
                    val pipeline = p0.pipeline()
                    pipeline.addLast(MyChannelHandler())
                }
            })
        }
    }

    /**
     * 自定义处理channel状态类
     * todo 后续拆解到外面去
     */
    inner class MyChannelHandler : ChannelInboundHandlerAdapter() {
        override fun channelActive(ctx: ChannelHandlerContext?) {
            super.channelActive(ctx)
            Log.d(TAG, ">>channelActive<<")
        }

        override fun channelInactive(ctx: ChannelHandlerContext?) {
            super.channelInactive(ctx)
            Log.d(TAG, ">>channelInactive<<")
            this@TcpServer.ctx = ctx
        }

        override fun channelRead(ctx: ChannelHandlerContext?, msg: Any?) {
            super.channelRead(ctx, msg)
            Log.d(TAG, ">>channelRead<<")
            msg ?: return
            if (msg is String) {
                Log.d(TAG, ">>read msg : $msg<<")
            }
        }

        override fun channelReadComplete(ctx: ChannelHandlerContext?) {
            super.channelReadComplete(ctx)
            Log.d(TAG, ">>channelReadComplete<<")
        }

        override fun channelRegistered(ctx: ChannelHandlerContext?) {
            super.channelRegistered(ctx)
            Log.d(TAG, ">>channelRegistered<<")
        }

        override fun channelUnregistered(ctx: ChannelHandlerContext?) {
            super.channelUnregistered(ctx)
            Log.d(TAG, ">>channelUnregistered<<")
        }

        override fun channelWritabilityChanged(ctx: ChannelHandlerContext?) {
            super.channelWritabilityChanged(ctx)
            Log.d(TAG, ">>channelWritabilityChanged<<")
        }
    }


    fun connect() {
        serverBootStrap.bind(Config.PORT)
    }

    fun send(s: String) {
        if (TextUtils.isEmpty(s)) return
        ctx?.let {
            if (it.channel().isActive) {
                it.writeAndFlush(s)
                Log.w(TAG, "服务端发送数据: $s")
            } else {
                Log.w(TAG, "ChannelHandlerContext channel未链接")
            }
            return
        }
        Log.w(TAG, "ChannelHandlerContext 未进行初始化")
    }

}