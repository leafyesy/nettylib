package com.example.nettylib.tcp.client.idle

import android.content.ContentValues.TAG
import android.util.Log
import androidx.annotation.NonNull
import com.example.nettylib.operator.ProtoBufClientOperator
import com.example.nettylib.tcp.Config
import io.netty.bootstrap.Bootstrap
import io.netty.channel.*
import java.util.concurrent.TimeUnit


/**
 * 重连检测狗，当发现当前的链路不稳定关闭之后，进行12次重连
 */
@ChannelHandler.Sharable
abstract class ConnectionWatchdog(
    private val bootstrap: Bootstrap,
    private val port: Int,
    private val host: String,
    private val clientOperator: ProtoBufClientOperator
) :
    ChannelInboundHandlerAdapter(), ChannelHandlerHolder {
    /**
     * 当前重连的次数
     */
    private var attempts: Int = 0
    /**
     * 最大重连次数
     */
    private val CONNECT_MAX_RETRY = 12
    /**
     * 重连间隔时间
     */
    private val CONNECT_RETRY_INTERVAL = 1000L

    private var isReconnecting: Boolean = false

    fun stop() {
    }

    override fun holdChannelPipeline(channelPipeline: ChannelPipeline) {
//        channelPipeline.addLast(
//            this,
//            CustomProtoBufEncoder(),
//            IdleStateHandler(Config.READ_IDLE_TIME, Config.WRITE_IDLE_TIME, 0, TimeUnit.SECONDS),
//            ConnectorIdleStateTrigger(),
//            ClientConnectionHandler(clientOperator),
//            CustomProtoBufDecoder(HeartBeatData.HeartBeat.getDefaultInstance()),
//            ClientDataHandler(clientOperator)
//        )
        clientOperator.handleChannelPipeline(channelPipeline)
    }

    /**
     * channel链路每次active的时候，将其连接的次数重新
     */
    @Throws(Exception::class)
    override fun channelActive(ctx: ChannelHandlerContext) {
        println("当前链路已经激活了，重连尝试次数重新置为0  old.attempts:$attempts")
        if (attempts == 0)
            ctx.fireChannelActive()
        attempts = 0
    }

    @Throws(Exception::class)
    override fun channelInactive(ctx: ChannelHandlerContext) {
        println("链接关闭")
        if (attempts <= CONNECT_MAX_RETRY) {
            reconnect()
        } else {
            attempts = 0
            ctx.fireChannelInactive()
        }
    }

    fun reconnect() {
        if (isReconnecting) {
            Log.d(TAG, "重连中...")
            return
        }
        isReconnecting = true
        connectOrRetry(bootstrap, host, port, CONNECT_MAX_RETRY)
    }

    /**
     * 连接服务器并等待连接结果
     * <br></br>
     * 连接失败进行[Config.CONNECT_MAX_RETRY]次重连,
     * 每隔 [Config.CONNECT_RETRY_INTERVAL] 秒建立连接
     *
     * @param bootstrap 客户端引导类对象
     * @param host      服务器主机
     * @param port      服务器端口
     * @param retry     重试次数
     * @param listener  客户端连接服务器的事务处理回调接口
     */
    private fun connectOrRetry(
        @NonNull bootstrap: Bootstrap, @NonNull host: String, port: Int,
        retry: Int
    ) {
        attempts = CONNECT_MAX_RETRY - retry
        Log.e(TAG, "第${attempts}次重连, 间隔延迟: $CONNECT_RETRY_INTERVAL 秒!")
        bootstrap.connect(host, port).addListener { future ->
            if (future is ChannelFuture)
                when {
                    future.isSuccess -> {
                        // 客户端连接渠道
                        Log.d(TAG, "连接成功!")
                        isReconnecting = false
                        future.channel()?.apply {
                            clientOperator.onChannelReConnectedSuccess(this)
                        }
                    }
                    retry == 0 -> {
                        Log.e(TAG, "重试次数已用完, 放弃连接!")
                        // 关闭客户端, 释放资源
                        // 连接服务器失败回调
                        isReconnecting = false
                        clientOperator.onChannelReConnectedFailed()
                    }
                    else -> {
                        // 第几次重连
                        val order = CONNECT_MAX_RETRY - retry + 1
                        // 本次重连的间隔
                        val delay = CONNECT_RETRY_INTERVAL
                        Log.e(TAG, "连接失败, 第" + order + "次重连, " + "间隔延迟: " + delay + "秒!")
                        bootstrap.config().group().schedule({
                            try {
                                connectOrRetry(bootstrap, host, port, retry - 1)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }, delay, TimeUnit.SECONDS)
                    }
                }
        }
    }

}
