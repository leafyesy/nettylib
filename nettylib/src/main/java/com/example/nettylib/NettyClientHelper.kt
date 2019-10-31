package com.example.nettylib

import android.content.Context
import android.util.Log
import com.example.nettylib.ssl.SecureChatSslContextFactory
import com.example.nettylib.ssl.SecureChatSslHandler
import com.example.nettylib.tcp.TcpProtoBufClient
import com.example.nettylib.udp.UdpHandler
import com.google.protobuf.MessageLite
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPipeline
import io.netty.channel.socket.DatagramPacket
import java.nio.charset.Charset
import java.util.concurrent.Executors

open class NettyClientHelper(val context: Context) {

    companion object {
        private val TAG = NettyClientHelper::class.java.simpleName
    }

    private var serverExecutor = Executors.newScheduledThreadPool(1)

    private val udpHandler by lazy {
        object : UdpHandler() {
            override fun onReadData(
                channelHandlerContext: ChannelHandlerContext,
                msg: DatagramPacket
            ) {
                super.onReadData(channelHandlerContext, msg)
                handleUpdData(msg)
            }
        }
    }

    private fun handleUpdData(msg: DatagramPacket) {
        Log.e(TAG, "udpHandler  onReadData 接收到数据!!")
        if (tcpProtoBufClient.isConnect()) {
            Log.w(TAG, "tcp链接已经连接 不需要重新连接 返回")
            return
        }
        val hostAddress = msg.sender().address.hostAddress
        val port = Constant.TCP_SERVICE_PORT

        //host/port一致的情况下 如果tcp在重连中 则不需要断开重连


        Log.e(
            TAG,
            "start Bind Tcp!!! post:${port}  ,   host:${hostAddress}"
        )
        tcpProtoBufClient.connect(
            Constant.TCP_SERVICE_PORT,
            msg.sender().address.hostAddress
        )//HeartBeatData.HeartBeat.getDefaultInstance()
    }

    private val tcpProtoBufClient by lazy {
        object : TcpProtoBufClient() {
            override fun onReadData(
                channelHandlerContext: ChannelHandlerContext,
                data: MessageLite
            ) {
                super.onReadData(channelHandlerContext, data)
                Log.e(TAG, "TcpProtoBufClient  onReadData  接收到数据!!")
            }

            override fun handleChannelPipeline(channelPipeline: ChannelPipeline) {
                super.handleChannelPipeline(channelPipeline)
                SecureChatSslHandler(context).secure(
                    "bksclient.keystore",
                    "bksclient.keystore",
                    channelPipeline
                )
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
            ssl()
            Log.d(TAG, "start client bind!!!")
            udpHandler.stop()
            udpHandler.bind(Constant.UDP_SERVICE_PLATE_PORT)
        }
    }

    private fun ssl() {
        SecureChatSslContextFactory.getClientContextFromAssets(
            context,
            "bksclient.keystore",
            "bksclient.keystore"
        )
    }

    @Throws(Exception::class)
    fun send(str: String) {
        tcpProtoBufClient.getChannelHandlerContext()?.let {
            tcpProtoBufClient.send(handlerData(it, str))
            return
        }
        throw Exception("tcp 连接 未初始化")
    }

    private fun handlerData(context: ChannelHandlerContext, str: String): ByteBuf {
        val bytes = str.toByteArray(Charset.forName("utf-8"))
        return context.alloc().buffer().apply {
            writeBytes(bytes)
        }
    }

    /**
     * 完全关闭链接
     * 宕机 主动断开连接 并释放资源
     */
    fun shutdown() {
        tcpProtoBufClient.disconnect()
        udpHandler.stop()
    }

}