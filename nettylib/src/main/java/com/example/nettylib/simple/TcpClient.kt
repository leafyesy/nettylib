package com.example.nettylib.simple

import android.text.TextUtils
import android.util.Log
import io.netty.bootstrap.Bootstrap
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.util.CharsetUtil

/**
 * Created by leafye on 2019-09-19.
 */
class TcpClient {

    companion object {
        private val TAG = TcpClient::class.java.simpleName
    }

    private val workerGroup by lazy { NioEventLoopGroup() }
    private var ctx: ChannelHandlerContext? = null

    private val bootstrap by lazy {
        Bootstrap().apply {
            group(workerGroup)
            channel(NioSocketChannel::class.java)
            option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            option(ChannelOption.TCP_NODELAY, true)
            option(ChannelOption.SO_KEEPALIVE, true)
            handler(object : ChannelInitializer<Channel>() {
                override fun initChannel(p0: Channel?) {
                    p0 ?: return
                    val pipeline = p0.pipeline()
                    pipeline.addLast(MyChannelInboundHandlerAdapter())
                }
            })
        }
    }

    fun connect() {
        bootstrap.connect("127.0.0.1", Config.PORT)
    }

    inner class MyChannelInboundHandlerAdapter : ChannelInboundHandlerAdapter() {
        override fun channelActive(ctx: ChannelHandlerContext?) {
            super.channelActive(ctx)
            Log.d(TAG, "channelActive")
            this@TcpClient.ctx = ctx
        }

        override fun channelInactive(ctx: ChannelHandlerContext?) {
            super.channelInactive(ctx)
            Log.d(TAG, "channelInactive")
        }

        override fun channelRead(ctx: ChannelHandlerContext?, msg: Any?) {
            super.channelRead(ctx, msg)
            Log.d(TAG, ">>channelRead<<")
            msg ?: return
            if (msg is ByteBuf) {
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


    fun send(s: String) {
        if (TextUtils.isEmpty(s)) return
        ctx?.let {
            if (it.channel().isActive) {
                val copiedBuffer = Unpooled.copiedBuffer(s, CharsetUtil.UTF_8)
                it.writeAndFlush(copiedBuffer)
                Log.w(TAG, "客户端发送数据: $s")
            } else {
                Log.w(TAG, "ChannelHandlerContext channel未链接")
            }
            return
        }
        Log.w(TAG, "ChannelHandlerContext 未进行初始化")
    }


}