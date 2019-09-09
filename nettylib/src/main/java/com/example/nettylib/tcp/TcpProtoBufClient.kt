package com.example.nettylib.tcp

import com.example.nettylib.adapter.ConnectorIdleStateTrigger
import com.example.nettylib.tcp.client.ClientChannelOperator
import com.example.nettylib.tcp.client.ClientConnectionHandler
import com.example.nettylib.tcp.client.ClientDataHandler
import com.example.nettylib.tcp.client.idle.ConnectionWatchdog
import com.google.protobuf.MessageLite
import io.netty.bootstrap.Bootstrap
import io.netty.channel.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.timeout.IdleStateHandler
import io.netty.util.concurrent.Future
import io.netty.util.internal.logging.InternalLoggerFactory
import io.netty.util.internal.logging.JdkLoggerFactory
import java.util.concurrent.TimeUnit

open class TcpProtoBufClient : ClientChannelOperator() {
    /**
     * 判断是否连接
     * @return
     */
    override fun isConnect(): Boolean {
        return if (channelHandlerContext != null) {
            channelHandlerContext!!.channel().isActive
        } else false
    }

    private val idleStateTrigger: ConnectorIdleStateTrigger

    private var group: EventLoopGroup? = null
    private var boot: Bootstrap? = null
    private var channelHandlerContext: ChannelHandlerContext? = null

    private var watchdog: ConnectionWatchdog? = null
    var host: String? = null
        private set

    init {
        InternalLoggerFactory.setDefaultFactory(JdkLoggerFactory.INSTANCE)
        idleStateTrigger = ConnectorIdleStateTrigger()
    }

    /**
     * 连接
     * @param port      端口
     * @param host      主机
     * @param prototype     接收数据的 protobuffer 类型
     * @throws Exception
     */
    @Throws(Exception::class)
    fun connect(port: Int, host: String, prototype: MessageLite, autoReconnect: Boolean): ChannelFuture {
        if (group != null)
            throw Exception("不能重复连接")

        this.host = host

        group = NioEventLoopGroup()
        boot = Bootstrap().apply b@{
            this@b.group(group!!)
                .channel(NioSocketChannel::class.java)
                .option(ChannelOption.TCP_NODELAY, true)
            watchdog = object : ConnectionWatchdog(this@b, port, host, autoReconnect) {
                override fun holdChannelPipeline(channelPipeline: ChannelPipeline) {
                    channelPipeline.addLast(
                        this,
                        CustomProtobufEncoder(),
                        IdleStateHandler(Config.READ_IDLE_TIME, Config.WRITE_IDLE_TIME, 0, TimeUnit.SECONDS),
                        idleStateTrigger,
                        ClientConnectionHandler(this@TcpProtoBufClient),
                        CustomProtobufDecoder(prototype),
                        ClientDataHandler(this@TcpProtoBufClient)
                    )
                    handleChannelPipeline(channelPipeline)
                }
            }
            this@b.handler(object : ChannelInitializer<Channel>() {
                //初始化channel
                @Throws(Exception::class)
                override fun initChannel(ch: Channel) {
                    watchdog?.holdChannelPipeline(ch.pipeline())
                }
            })
        }
        return boot!!.connect(host, port)
    }

    /**
     * 关闭连接
     * @throws Exception
     */
    fun disconnect() {
        if (group == null)
            return
        channelHandlerContext = null
        watchdog?.stop()
        group!!.shutdownGracefully()
        group = null
    }

    override fun hasConnect(channelHandlerContext: ChannelHandlerContext) {
        this.channelHandlerContext = channelHandlerContext
        onConnect()
    }

    override fun hasDisconnect(channelHandlerContext: ChannelHandlerContext) {
        this.channelHandlerContext = null
        onDisconnect()
    }


    /**
     * 给子类重写 连接成功
     */
    open fun onConnect() {}

    /**
     * 子类重新 断开连接
     */
    open fun onDisconnect() {}

    /**
     * 子类重写 接收数据
     * @param channelHandlerContext
     * @param data
     */
    override fun onReadData(channelHandlerContext: ChannelHandlerContext, data: MessageLite) {}

    /**
     * 发送数据
     * @param messageLite
     * @return
     * @throws Exception
     */
    override fun send(messageLite: MessageLite): Future<*>? {
        return if (!isConnect()) null else channelHandlerContext!!.writeAndFlush(messageLite)
    }
}
