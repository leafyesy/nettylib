package com.example.nettylib

import android.content.Context
import android.util.Log
import com.example.nettylib.proto.HeartBeatData
import com.example.nettylib.ssl.SecureChatSslContextFactory
import com.example.nettylib.tcp.TcpProtoBufClient
import com.example.nettylib.udp.UdpHandler
import com.google.protobuf.MessageLite
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPipeline
import io.netty.channel.socket.DatagramPacket
import io.netty.handler.ssl.SslHandler
import java.lang.Exception
import java.util.concurrent.Executors

open class NettyClientHelper(val context: Context) {

    companion object {
        private val TAG = NettyClientHelper::class.java.simpleName
    }

    private var serverExecutor = Executors.newScheduledThreadPool(1)


    private val udpHandler by lazy {
        object : UdpHandler() {
            override fun onReadData(
                channelHandlerContext: ChannelHandlerContext, msg: DatagramPacket
            ) {
                super.onReadData(channelHandlerContext, msg)
                Log.e(TAG, "udpHandler  onReadData 接收到数据!!")
                if (tcpProtoBufClient.isConnect()) return
                tcpProtoBufClient.disconnect()
                Log.e(
                    TAG,
                    "udpHandler  start Bind Tcp!!! post:${Constant.TCP_SERVICE_PORT}  ,   host:${msg.sender().address.hostAddress}"
                )
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
        object : TcpProtoBufClient() {
            override fun onReadData(channelHandlerContext: ChannelHandlerContext, data: MessageLite) {
                super.onReadData(channelHandlerContext, data)
                Log.e(TAG, "TcpProtoBufClient  onReadData  接收到数据!!")
            }

            override fun handleChannelPipeline(channelPipeline: ChannelPipeline) {
                super.handleChannelPipeline(channelPipeline)
                try {
                    val engine = SecureChatSslContextFactory
                        .getClientContextFromAssets(context, "bksclient.keystore", "bksclient.keystore")
                        .createSSLEngine()
                    engine.useClientMode = true
                    channelPipeline.addFirst("ssl", SslHandler(engine))
                }catch (e:Exception){
                    e.printStackTrace()
                }

            }

            override fun hasDisconnect(channelHandlerContext: ChannelHandlerContext) {
                super.hasDisconnect(channelHandlerContext)
                Log.e(TAG, "client hasDisconnect")
            }

            override fun hasConnect(channelHandlerContext: ChannelHandlerContext) {
                super.hasConnect(channelHandlerContext)
                Log.e(TAG, "client hasConnect")
            }
        }
    }


    fun bind() {
        serverExecutor.execute {
            Log.d(TAG, "start client bind!!!")
            udpHandler.stop()
            udpHandler.bind(Constant.UDP_SERVICE_PLATE_PORT)
        }
    }

    fun ssl() {
        serverExecutor.execute {
            SecureChatSslContextFactory.getClientContextFromAssets(
                context,
                "bksclient.keystore",
                "bksclient.keystore"
            )
        }
    }

    fun shutdown() {
        tcpProtoBufClient.disconnect()
        udpHandler.stop()
    }

}