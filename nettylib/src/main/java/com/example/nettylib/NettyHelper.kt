package com.example.nettylib

import android.util.Log
import com.example.nettylib.proto.HeartBeatData
import com.example.nettylib.tcp.TcpProtobufClient
import com.example.nettylib.udp.UdpHandler
import com.google.protobuf.Message
import com.google.protobuf.MessageLite
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.socket.DatagramPacket

class NettyHelper {

    companion object {
        private val TAG = NettyHelper::class.java.simpleName
    }

    private val udpHandler by lazy {
        object : UdpHandler() {
            override fun onReadData(
                channelHandlerContext: ChannelHandlerContext, msg: DatagramPacket
            ) {
                super.onReadData(channelHandlerContext, msg)
                if (tcpProtoBufClient.isConnect()) return
                tcpProtoBufClient.disconnect()
                tcpProtoBufClient.connect(
                    Constant.TCP_SERVICE_PORT,
                    msg.sender().address.hostAddress,
                    HeartBeatData.HeartBeat.getDefaultInstance(),
                    true
                )
            }
        }
    }

    private val tcpProtoBufClient by lazy {
        object : TcpProtobufClient() {
            override fun onReadData(channelHandlerContext: ChannelHandlerContext, data: MessageLite) {
                super.onReadData(channelHandlerContext, data)
                Log.e(TAG, "接收到数据!!")
                //when (data.)
            }
        }
    }


    fun bind() {
        udpHandler.stop()
        udpHandler.bind(Constant.UDP_SERVICE_PLATE_PORT)
    }


}