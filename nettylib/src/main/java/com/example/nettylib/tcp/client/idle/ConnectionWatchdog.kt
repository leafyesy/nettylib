package com.example.nettylib.tcp.client.idle

import io.netty.bootstrap.Bootstrap
import io.netty.channel.*
import io.netty.util.HashedWheelTimer
import io.netty.util.Timeout
import io.netty.util.Timer
import io.netty.util.TimerTask
import java.util.concurrent.TimeUnit


/**
 * 重连检测狗，当发现当前的链路不稳定关闭之后，进行12次重连
 */
@ChannelHandler.Sharable
abstract class ConnectionWatchdog(
    private val bootstrap: Bootstrap,
    private val port: Int,
    private val host: String,
    reconnect: Boolean
) :
    ChannelInboundHandlerAdapter(), TimerTask, ChannelHandlerHolder {
    private val timer: Timer

    @Volatile
    private var reconnect = true
    private var attempts: Int = 0

    private var reconnectTimes = 10


    init {
        this.timer = HashedWheelTimer()
        this.reconnect = reconnect
    }

    fun stop() {
        this.reconnect = false
        timer.stop()
    }

    fun setReconnectTimes(reconnectTimes: Int) {
        this.reconnectTimes = reconnectTimes
    }

    /**
     * channel链路每次active的时候，将其连接的次数重新
     */
    @Throws(Exception::class)
    override fun channelActive(ctx: ChannelHandlerContext) {
        println("当前链路已经激活了，重连尝试次数重新置为0")
        if (attempts == 0)
            ctx.fireChannelActive()
        attempts = 0
    }

    @Throws(Exception::class)
    override fun channelInactive(ctx: ChannelHandlerContext) {
        println("链接关闭")
        if (startReConnected()) return
        attempts = 0
        ctx.fireChannelInactive()
    }

    fun startReConnected(): Boolean {
        if (reconnect && attempts < reconnectTimes) {
            println("链接关闭，将进行重连")
            attempts++
            //重连的间隔时间会越来越长
            val timeout = 2 shl attempts
            timer.newTimeout(this, timeout.toLong(), TimeUnit.MILLISECONDS)
            return true
        }
        return false
    }

    @Throws(Exception::class)
    override fun run(timeout: Timeout) {
        println(">>>开始重连<<<")
        val future: ChannelFuture
        //bootstrap已经初始化好了，只需要将handler填入就可以了
        synchronized(bootstrap) {
            bootstrap.handler(object : ChannelInitializer<Channel>() {

                @Throws(Exception::class)
                override fun initChannel(ch: Channel) {
                    holdChannelPipeline(ch.pipeline())
                }
            })
            future = bootstrap.connect(host, port)
        }
        //future对象
        future.addListener { f ->
            val succeed = f.isSuccess
            //如果重连失败，则调用ChannelInactive方法，再次出发重连事件，一直尝试12次，如果失败则不再重连
            if (!succeed) {
                println("重连失败")
                (f as ChannelFuture).channel().pipeline().fireChannelInactive()
            } else {
                println("重连成功")
            }
        }
    }

}
