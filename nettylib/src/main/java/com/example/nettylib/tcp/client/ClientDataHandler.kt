package com.example.nettylib.tcp.client

import android.util.Log
import com.example.nettylib.operator.ProtoBufClientOperator
import com.example.nettylib.proto.HeartBeatData
import com.google.protobuf.MessageLite
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler

class ClientDataHandler(private val clientOperator: ProtoBufClientOperator) :
    SimpleChannelInboundHandler<MessageLite>() {

    companion object {
        private val TAG = ClientDataHandler::class.java.simpleName
    }

    @Throws(Exception::class)
    override fun channelRead0(ctx: ChannelHandlerContext, msg: MessageLite) {
        if (msg !is HeartBeatData.HeartBeat) {//不是心跳
            clientOperator.channelRead0(ctx, msg)
        } else {
            Log.d(TAG, "心跳")
        }
    }
}
