package com.example.nettylib.udp

import android.text.TextUtils
import android.util.Log
import com.example.nettylib.NettyUtils
import io.netty.bootstrap.Bootstrap
import io.netty.buffer.Unpooled.wrappedBuffer
import io.netty.channel.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.DatagramChannel
import io.netty.channel.socket.DatagramPacket
import io.netty.channel.socket.nio.NioDatagramChannel
import io.netty.util.internal.SocketUtils
import io.netty.util.internal.logging.InternalLoggerFactory
import io.netty.util.internal.logging.JdkLoggerFactory

open class UdpHandler : DataOperator {

    companion object {
        private const val TAG: String = "UdpHandler"
    }

    private var channel: Channel? = null
    private var group: EventLoopGroup? = null

    init {
        InternalLoggerFactory.setDefaultFactory(JdkLoggerFactory.INSTANCE)
    }

    /**
     * 绑定
     * @param port
     */
    @Throws(Exception::class)
    fun bind(port: Int) {
        group = NioEventLoopGroup()
        val b = Bootstrap()
        b.group(group!!)
            .channel(NioDatagramChannel::class.java)
            .option(ChannelOption.SO_BROADCAST, true)//广播的形式-udp
            .option(ChannelOption.SO_REUSEADDR, true)
            .handler(object : ChannelInitializer<DatagramChannel>() {
                @Throws(Exception::class)
                override fun initChannel(datagramChannel: DatagramChannel) {
                    val channelPipeline = datagramChannel.pipeline()
                    channelPipeline.addLast(DatagramPacketHandler(this@UdpHandler))
                }
            })
        channel = b.bind(port).sync().channel()
    }

    /**
     * 停止
     */
    fun stop() {
        try {
            channel?.close()
            channel = null
            group?.shutdownGracefully()?.sync()
            group = null
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    /**
     * 广播
     * @param data 广播数据
     * @param port 广播端口
     * @return
     */
    fun broadcast(data: String?, port: Int, isWifiOn: Boolean): ChannelFuture? {
        var newData = data
        if (channel == null)
            return null
        if (data == null)
            newData = "-"
        var broadcastAddr = if (isWifiOn) {
            "255.255.255.255"
        } else {
            "192.168.43.255"
        }
        val routeBroadcastAddress = NettyUtils.getRouteBroadcastAddress()
        if (!TextUtils.isEmpty(routeBroadcastAddress)) {
            broadcastAddr = routeBroadcastAddress
        }
        Log.d(TAG, "data:$data + broadcastAddr:$broadcastAddr")
        val datagramPacket = DatagramPacket(
            wrappedBuffer(newData!!.toByteArray()),
            SocketUtils.socketAddress(broadcastAddr, port)
        )
        return channel!!.writeAndFlush(datagramPacket)
    }

    /**
     * 发送数据
     * @param data
     * @param port
     * @param address
     * @return
     */
    fun sendData(data: String?, port: Int, address: String): ChannelFuture? {
        val newData = data ?: ""
        return channel?.writeAndFlush(
            DatagramPacket(
                wrappedBuffer(newData.toByteArray()),
                SocketUtils.socketAddress(address, port)
            )
        )
    }


    override fun onReadData(channelHandlerContext: ChannelHandlerContext, msg: DatagramPacket) {

    }
}
