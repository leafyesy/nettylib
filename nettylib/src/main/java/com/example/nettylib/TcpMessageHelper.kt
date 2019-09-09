package com.example.nettylib

import android.util.Log
import com.example.nettylib.tcp.TcpProtobufClient
import com.google.protobuf.MessageLite
import io.netty.channel.ChannelHandlerContext

class TcpMessageHelper : TcpProtobufClient() {

    companion object {
        private val TAG = TcpMessageHelper::class.java.simpleName
    }

    override fun onReadData(channelHandlerContext: ChannelHandlerContext, data: MessageLite) {
        super.onReadData(channelHandlerContext, data)
        Log.e(TAG, "接收了数据")
    }
}