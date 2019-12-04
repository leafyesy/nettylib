package com.example.nettylib.tcp.server

import android.util.Log
import com.example.nettylib.proto.HeartBeatData
import com.google.protobuf.MessageLite
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler

class ServerDataHandler(private val serverChannelOperator: ServerChannelOperator?) :
    SimpleChannelInboundHandler<MessageLite>() {

    companion object {
        private val TAG = ServerDataHandler::class.java.simpleName
    }

    @Throws(Exception::class)
    override fun channelRead0(ctx: ChannelHandlerContext, msg: MessageLite) {
        Log.i("ServerDataHandler", "msg:$msg")
        if (msg !is HeartBeatData.HeartBeat) {
            //不是心跳
            serverChannelOperator?.onReadData(ctx, msg)
        } else {
            Log.d(TAG, "心跳")
        }
    }
}
