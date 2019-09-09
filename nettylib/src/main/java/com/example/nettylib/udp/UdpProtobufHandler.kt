package com.example.nettylib.udp

import com.google.protobuf.MessageLite
import io.netty.bootstrap.Bootstrap
import io.netty.channel.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioDatagramChannel
import io.netty.util.internal.logging.InternalLoggerFactory
import io.netty.util.internal.logging.JdkLoggerFactory

class UdpProtobufHandler : ProtoDataOperator {

    private var channel: Channel? = null
    private var group: EventLoopGroup? = null

    init {
        InternalLoggerFactory.setDefaultFactory(JdkLoggerFactory.INSTANCE)
    }

    /**
     * 绑定
     * @param protoType
     * @param port
     * @throws Exception
     */
    @Throws(Exception::class)
    fun bind(port: Int, protoType: MessageLite) {
        group = NioEventLoopGroup()
        val b = Bootstrap()
        b.group(group!!)
            .channel(NioDatagramChannel::class.java)
            .option(ChannelOption.SO_BROADCAST, true)
            .handler(UdpProtoChannelInitializer(protoType, this))

        channel = b.bind(port).sync().channel()
    }
    //
    //    public void bind(int port) throws Exception{
    //        group = new NioEventLoopGroup();
    //        Bootstrap b = new Bootstrap();
    //        b.group(group)
    //                .channel(NioDatagramChannel.class)
    //                .option(ChannelOption.SO_BROADCAST, true)
    //                .handler(new UdpProtoChannelInitializer());
    //
    //        channel = b.bind(port).sync().channel();
    //    }

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
    fun broadcast(data: MessageLite, port: Int): ChannelFuture? {
        return if (channel == null) null else channel!!.writeAndFlush(ProtoDataPacket("255.255.255.255", port, data))

    }

    /**
     * 发送数据
     * @param data
     * @param port
     * @param address
     * @return
     */
    fun sendData(data: MessageLite, port: Int, address: String): ChannelFuture? {
        return if (channel == null) null else channel!!.writeAndFlush(ProtoDataPacket(address, port, data))

    }

    override fun onReadData(channelHandlerContext: ChannelHandlerContext, messageLite: MessageLite) {}
}