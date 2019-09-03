package com.example.nettylib.tcp.server

import com.google.protobuf.MessageLite
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPipeline
import java.util.ArrayList
import java.util.concurrent.CopyOnWriteArrayList

abstract class ServerChannelOperator() {
    private var channelList: CopyOnWriteArrayList<ChannelHandlerContext> = CopyOnWriteArrayList()

    /**
     * 新连接接入
     * @param channelHandlerContext
     */
    fun holdChildChannel(channelHandlerContext: ChannelHandlerContext) {
        channelList.add(channelHandlerContext)
        onChannelConnect(channelHandlerContext)
    }

    /**
     * 释放连接
     * @param channelHandlerContext
     */
    fun releaseChildChannel(channelHandlerContext: ChannelHandlerContext) {
        channelList.remove(channelHandlerContext)
        onChannelDisconnect(channelHandlerContext)
    }

    /**
     * 广播数据
     * @param messageLite
     * @return
     */
    fun broadcast(messageLite: MessageLite): List<ChannelFuture> {

        val channelFutureList = ArrayList<ChannelFuture>()
        for (channelHandlerContext in channelList) {
            channelFutureList.add(channelHandlerContext.writeAndFlush(messageLite))
        }

        return channelFutureList
    }

    /**
     * 同步发送
     * @param messageLite
     */
    fun broadcastSync(messageLite: MessageLite) {
        for (channelHandlerContext in channelList) {
            if (channelHandlerContext.channel().isActive) {
                try {
                    channelHandlerContext.writeAndFlush(messageLite).sync()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }

    /**
     * 获取所有连接
     * @return
     */
    fun getAllChannel(): List<ChannelHandlerContext> {
        return channelList
    }

    /**
     * 子类重写后处理handler链
     * @param channelPipeline
     */
    fun handleChannelPipeline(channelPipeline: ChannelPipeline) {

    }

    /**
     * 子类重写处理连接成功
     * @param channelHandlerContext
     */
    fun onChannelConnect(channelHandlerContext: ChannelHandlerContext) {

    }

    /**
     * 子类重写处理连接失败
     * @param channelHandlerContext
     */
    fun onChannelDisconnect(channelHandlerContext: ChannelHandlerContext) {

    }

    /**
     * 接收数据
     * @param channelHandlerContext
     * @param messageLite
     */
    abstract fun onReadData(channelHandlerContext: ChannelHandlerContext, messageLite: MessageLite)
}

