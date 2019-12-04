package com.example.nettylib.websocket

import android.util.Log
import com.example.nettylib.decodeandencode.CustomMessageToMessageDecoder
import com.example.nettylib.decodeandencode.CustomMessageToMessageEncoder
import com.example.nettylib.handler.idog.ConnectionWatchdog
import com.example.nettylib.operator.ProtoBufClientOperator
import com.example.nettylib.tcp.client.ClientChannelOperator
import com.example.nettylib.websocket.client.WebSocketClientHandler
import com.example.nettylib.websocket.client.WebsocketWatchDogImp
import com.google.protobuf.MessageLite
import io.netty.bootstrap.Bootstrap
import io.netty.channel.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.http.HttpClientCodec
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler
import io.netty.util.concurrent.Future
import java.net.URI


/**
 * Created by leafye on 2019-11-28.
 */
open class WebSocketProtoBufClient : ClientChannelOperator() {

    private var group: EventLoopGroup? = null
    private var boot: Bootstrap? = null
    private var channelHandlerContext: ChannelHandlerContext? = null//重连上下文
    private var watchdog: ConnectionWatchdog? = null//重连检测狗
    private var host: String? = null//链接的host地址
    private var channel: Channel? = null//tcp链接成功后的通道
    private var handler: WebSocketClientHandler? = null

    private val TAG = WebSocketProtoBufClient::class.java.simpleName

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
        if (group != null) {
            Log.w(TAG, "已经绑定了...")
            return;
            //throw Exception("不能重复连接")
        }
        val target = "ws://$host:$port/websocket"
        Log.d(TAG, "target:$target")
        val uri = URI(target)
        if (group != null)
            throw Exception("不能重复连接")
        handler = WebSocketClientHandler(uri)

        this.host = host
        group = NioEventLoopGroup()
        boot = Bootstrap().apply b@{
            this@b.group(group!!)
                .channel(NioSocketChannel::class.java)
                .option(ChannelOption.TCP_NODELAY, true)//tcp消息不延迟
                .option(ChannelOption.SO_REUSEADDR, true)//端口复用
                .option(ChannelOption.SO_KEEPALIVE, true)//保持链接
            val watchDogImp = WebsocketWatchDogImp(handler!!)
            watchdog = ConnectionWatchdog(this@b, port, host, clientOperator, watchDogImp)
            this@b.handler(object : ChannelInitializer<Channel>() {
                //初始化channel
                @Throws(Exception::class)
                override fun initChannel(ch: Channel) {
                    ch.pipeline().apply {
                        addLast(
                            watchdog,
                            HttpClientCodec(),
                            HttpObjectAggregator(8192),
                            WebSocketClientCompressionHandler.INSTANCE
                            //ClientConnectionHandler(clientOperator)
                        )
                        addLast("decode", CustomMessageToMessageDecoder())
                        addLast("encode", CustomMessageToMessageEncoder())
                        addLast("handler", handler)
                    }
                }
            })
        }
        watchdog?.reconnect()
//        Log.d(TAG, "开始请求!!")
//        ch = boot?.connect(host, port)?.sync()?.channel()
//        Log.d(TAG, "链接成功!!")
//        handler?.handshakeFuture()?.sync()
//        Log.d(TAG, "handshakeFuture 成功!!")
    }

    var ch: Channel? = null

    fun isNeedReConnect(host: String, port: Int) {

    }

    private val clientOperator: ProtoBufClientOperator by lazy {
        object : ProtoBufClientOperator {
            override fun onChannelReConnectedFailed() {
                //关闭链接 释放资源
                //重连失败
                Log.d(TAG, "clientOperator  onChannelReConnectedFailed")
            }

            override fun onChannelReConnectedSuccess(channel: Channel) {
                this@WebSocketProtoBufClient.channel = channel
                ch = channel
                Log.i(TAG, "channel 初始化 !!! ch:$ch")
            }

            override fun handleChannelPipeline(channelPipeline: ChannelPipeline) {
            }

            override fun channelActive(ctx: ChannelHandlerContext) {
                Log.d(TAG, "clientOperator  channelActive")
                //
            }

            override fun channelInactive(ctx: ChannelHandlerContext) {
                hasDisconnect(ctx)
                Log.d(TAG, "clientOperator  channelInactive")
            }

            override fun channelRead0(ctx: ChannelHandlerContext, msg: MessageLite) {
                onReadData(ctx, msg)
                Log.d(TAG, "clientOperator  channelRead0")
            }
        }
    }

    /**
     * 判断是否连接
     * @return
     */
    override fun isConnect(): Boolean = ch?.isActive == true

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
        return if (!isConnect()) null else ch?.writeAndFlush(messageLite)
    }

    /**
     * 发送byteBuf数据
     */
    override fun send(byteBuf: Any): Future<*>? {
        return if (ch == null) {
            Log.i(TAG, "channel 未初始化!!!")
            null
        } else {
            Log.i(TAG, "byteBuf:$byteBuf")
            ch?.writeAndFlush(byteBuf)
        }
    }

}