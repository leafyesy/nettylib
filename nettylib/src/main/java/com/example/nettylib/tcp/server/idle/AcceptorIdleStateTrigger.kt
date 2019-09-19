package com.example.nettylib.tcp.server.idle

import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.handler.timeout.IdleState
import io.netty.handler.timeout.IdleStateEvent

@ChannelHandler.Sharable
class AcceptorIdleStateTrigger : ChannelInboundHandlerAdapter() {

    @Throws(Exception::class)
    override fun userEventTriggered(ctx: ChannelHandlerContext, evt: Any) {
        if (evt is IdleStateEvent) {
            val state = evt.state()
            if (state == IdleState.READER_IDLE) {
                throw Exception("idle exception")
            }
        } else {
            super.userEventTriggered(ctx, evt)
        }
    }
}