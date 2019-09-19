package com.example.nettylib.udp

import io.netty.bootstrap.Bootstrap
import io.netty.buffer.Unpooled
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
            .option(ChannelOption.SO_BROADCAST, true)
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
        if (channel != null) {
            channel!!.close()
        }
        try {
            if (group != null)
                group!!.shutdownGracefully().sync()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }

    /**
     * 广播
     * @param data
     * @param port
     * @return
     */
    fun broadcast(data: String?, port: Int, isWifiOn: Boolean): ChannelFuture? {
        var newData = data
        if (channel == null)
            return null
        if (data == null)
            newData = "-"
        return if (isWifiOn) {
            channel!!.writeAndFlush(
                DatagramPacket(
                    wrappedBuffer(newData!!.toByteArray()),
                    SocketUtils.socketAddress("255.255.255.255", port)
                )
            )
        } else channel!!.writeAndFlush(
            DatagramPacket(
                wrappedBuffer(newData!!.toByteArray()),
                SocketUtils.socketAddress("192.168.43.255", port)
            )
        )
    }

    /**
     * 发送数据
     * @param data
     * @param port
     * @param address
     * @return
     */
    fun sendData(data: String?, port: Int, address: String): ChannelFuture? {
        var newData = data
        if (channel == null)
            return null
        if (data == null)
            newData = ""
        return channel!!.writeAndFlush(
            DatagramPacket(
                wrappedBuffer(newData!!.toByteArray()),
                SocketUtils.socketAddress(address, port)
            )
        )
    }


    override fun onReadData(channelHandlerContext: ChannelHandlerContext, msg: DatagramPacket) {

    }
}
