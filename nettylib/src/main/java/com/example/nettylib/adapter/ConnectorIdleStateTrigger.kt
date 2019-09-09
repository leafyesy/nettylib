package com.example.nettylib.adapter

import com.example.nettylib.proto.HeartBeatData
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.handler.timeout.IdleState
import io.netty.handler.timeout.IdleStateEvent

class ConnectorIdleStateTrigger : ChannelInboundHandlerAdapter() {

    @Throws(Exception::class)
    override fun userEventTriggered(ctx: ChannelHandlerContext, evt: Any) {
        if (evt is IdleStateEvent) {
            val state = evt.state()
            if (state == IdleState.WRITER_IDLE) {
                // write heartbeat to server
                val heartBeatBuilder = HeartBeatData.HeartBeat.newBuilder()
                heartBeatBuilder.time = System.currentTimeMillis()
                ctx.writeAndFlush(heartBeatBuilder.build())
            } else if (state == IdleState.READER_IDLE) {
                throw Exception("idle exception")
            }
        } else {
            super.userEventTriggered(ctx, evt)
        }
    }
}