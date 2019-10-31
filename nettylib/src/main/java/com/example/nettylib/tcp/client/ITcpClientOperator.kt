package com.example.nettylib.tcp.client

import com.google.protobuf.MessageLite
import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPipeline

/**
 * Created by leafye on 2019-10-26.
 */
interface ITcpClientOperator {
    /**
     * 连接生效
     */
    fun channelActive(ctx: ChannelHandlerContext)

    /**
     * 断开连接
     */
    fun channelInactive(ctx: ChannelHandlerContext)

    /**
     * 读取tcp数据
     */
    fun channelRead0(ctx: ChannelHandlerContext, msg: MessageLite)

    /**
     * 处理数据通道
     */
    fun handleChannelPipeline(channelPipeline: ChannelPipeline)

    /**
     * 重连失败
     */
    fun onChannelReConnectedFailed()

    /**
     * 重连成功
     */
    fun onChannelReConnectedSuccess(channel: Channel)

}

