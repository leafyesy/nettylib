package com.example.nettylib.tcp.client.idle

import android.content.ContentValues.TAG
import android.util.Log
import com.example.nettylib.ConnectOrRetryCallback
import com.example.nettylib.IWatchDog
import com.example.nettylib.operator.ProtoBufClientOperator
import io.netty.bootstrap.Bootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter


/**
 * 重连检测狗，当发现当前的链路不稳定关闭之后，进行12次重连
 */
@ChannelHandler.Sharable
class ConnectionWatchdog(
    private val bootstrap: Bootstrap,
    private val port: Int,
    private val host: String,
    private val clientOperator: ProtoBufClientOperator,
    private val watchDogImp: IWatchDog
) : ChannelInboundHandlerAdapter(),
    IWatchDog by watchDogImp {

    init {
        watchDogImp.registerConnectOrRetryCallback(object : ConnectOrRetryCallback {
            override fun success(channel: Channel) {
                clientOperator.onChannelReConnectedSuccess(channel)
            }

            override fun failure() {
                clientOperator.onChannelReConnectedFailed()
            }

            override fun startReConnect(count: Int) {

            }
        })
    }

    /**
     * channel链路每次active的时候，将其连接的次数重新
     */
    @Throws(Exception::class)
    override fun channelActive(ctx: ChannelHandlerContext) {
        println("当前链路已经激活了，重连尝试次数重新置为0  old.attempts:${attempt}")
        if (attempt == 0)
            ctx.fireChannelActive()
        attempt = 0
    }

    @Throws(Exception::class)
    override fun channelInactive(ctx: ChannelHandlerContext) {
        println("链接关闭")
        if (attempt <= connectMaxRetry) {
            reconnect()
        } else {
            attempt = 0
            ctx.fireChannelInactive()
        }
    }

    fun reconnect() {
        if (isReconnecting) {
            Log.d(TAG, "重连中...")
            return
        }
        isReconnecting = true
        connectOrRetry(bootstrap, host, port, connectMaxRetry)
    }

}
