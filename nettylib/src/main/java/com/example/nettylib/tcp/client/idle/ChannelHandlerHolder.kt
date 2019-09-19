package com.example.nettylib.tcp.client.idle

import io.netty.channel.ChannelPipeline

interface ChannelHandlerHolder {

    fun holdChannelPipeline(channelPipeline: ChannelPipeline)
}
