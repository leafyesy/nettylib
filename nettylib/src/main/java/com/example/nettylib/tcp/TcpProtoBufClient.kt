package com.example.nettylib.tcp

import com.example.nettylib.adapter.ConnectorIdleStateTrigger
import com.example.nettylib.operator.ProtoBufClientOperator
import com.example.nettylib.proto.HeartBeatData
import com.example.nettylib.tcp.client.ClientChannelOperator
import com.example.nettylib.tcp.client.ClientConnectionHandler
import com.example.nettylib.tcp.client.ClientDataHandler
import com.example.nettylib.handler.idog.ConnectionWatchdog
import com.example.nettylib.tcp.client.TcpWatchDogImp
import com.google.protobuf.MessageLite
import io.netty.bootstrap.Bootstrap
import io.netty.channel.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.timeout.IdleStateHandler
import io.netty.util.concurrent.Future
import java.util.concurrent.TimeUnit

open class TcpProtoBufClient : ClientChannelOperator() {

    private var group: EventLoopGroup? = null
    private var boot: Bootstrap? = null
    private var channelHandlerContext: ChannelHandlerContext? = null//重连上下文
    private var watchdog: ConnectionWatchdog? = null//重连检测狗
    private var host: String? = null//链接的host地址
    private var channel: Channel? = null//tcp链接成功后的通道

    fun getChannelHandlerContext(): ChannelHandlerContext? = channelHandlerContext

    /**
     * 连接
     * @param port      端口
     * @param host      主机
     * @param prototype     接收数据的 protobuffer 类型
     * @throws Exception
     */
    @Throws(Exception::class)
    fun connect(
        port: Int,
        host: String
        //prototype: MessageLite
    ) {
        if (group != null)
            throw Exception("不能重复连接")
        this.host = host
        group = NioEventLoopGroup()
        boot = Bootstrap().apply b@{
            this@b.group(group!!)
                .channel(NioSocketChannel::class.java)
                .option(ChannelOption.TCP_NODELAY, true)//tcp消息不延迟
                .option(ChannelOption.SO_REUSEADDR, true)//端口复用
                .option(ChannelOption.SO_KEEPALIVE, true)//保持链接
            watchdog = ConnectionWatchdog(
                this@b,
                port,
                host,
                tcpClientOperator,
                TcpWatchDogImp()
            )
            this@b.handler(object : ChannelInitializer<Channel>() {
                //初始化channel
                @Throws(Exception::class)
                override fun initChannel(ch: Channel) {
                    ch.pipeline().addLast(
                        watchdog,
                        CustomProtoBufEncoder(),
                        IdleStateHandler(
                            Config.READ_IDLE_TIME,
                            Config.WRITE_IDLE_TIME,
                            0,
                            TimeUnit.SECONDS
                        ),
                        ConnectorIdleStateTrigger(),
                        ClientConnectionHandler(tcpClientOperator),
                        CustomProtoBufDecoder(HeartBeatData.HeartBeat.getDefaultInstance()),
                        ClientDataHandler(tcpClientOperator)
                    )
                }
            })
        }
        watchdog?.reconnect()
    }


    fun isNeedReConnect(host: String, port: Int) {

    }


    private val tcpClientOperator: ProtoBufClientOperator by lazy {
        object : ProtoBufClientOperator {
            override fun onChannelReConnectedFailed() {
                //关闭链接 释放资源
                //重连失败

            }

            override fun onChannelReConnectedSuccess(channel: Channel) {
                this@TcpProtoBufClient.channel = channel
            }

            override fun handleChannelPipeline(channelPipeline: ChannelPipeline) {
                this@TcpProtoBufClient.handleChannelPipeline(channelPipeline)
            }

            override fun channelActive(ctx: ChannelHandlerContext) {
                hasConnect(ctx)
            }

            override fun channelInactive(ctx: ChannelHandlerContext) {
                hasDisconnect(ctx)
            }

            override fun channelRead0(ctx: ChannelHandlerContext, msg: MessageLite) {
                onReadData(ctx, msg)
            }
        }
    }

    /**
     * 判断是否连接
     * @return
     */
    override fun isConnect(): Boolean = channelHandlerContext?.channel()?.isActive == true

    /**
     * 关闭连接
     * @throws Exception
     */
    fun disconnect() {
        if (group == null)
            return
        channelHandlerContext = null
        //watchdog?.stop()
        group?.shutdownGracefully()
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
        return if (!isConnect()) null else channelHandlerContext?.writeAndFlush(messageLite)
    }

    /**
     * 发送byteBuf数据
     */
    override fun send(byteBuf: Any): Future<*>? {
        return if (!isConnect()) null else channelHandlerContext?.writeAndFlush(byteBuf)
    }
}
