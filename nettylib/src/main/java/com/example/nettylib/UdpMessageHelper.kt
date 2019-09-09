package com.example.nettylib

import android.os.Build
import android.util.Log
import com.example.nettylib.udp.UdpHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.socket.DatagramPacket

class UdpMessageHelper : UdpHandler() {



    override fun onReadData(channelHandlerContext: ChannelHandlerContext, msg: DatagramPacket) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Log.d(
                "udp",
                msg.sender().hostName + "=" + msg.sender().hostString + "=" + msg.sender().address.hostAddress
            )
        }
        if (plateService.tcpMessageHelper.isConnect())
            return
        try {
            plateService.tcpMessageHelper.disconnect()
            plateService.tcpMessageHelper.connect(
                CommandConfig.TCP_SERVICE_PORT,
                msg.sender().address.hostAddress,
                MessageData.Message.getDefaultInstance(),
                true
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}

