package com.example.nettylib.handler.idog

import androidx.annotation.NonNull
import io.netty.bootstrap.Bootstrap
import io.netty.channel.Channel

/**
 * Created by leafye on 2019-12-05.
 * 重连检测狗接口
 */
interface IWatchDog {

    companion object {
        /**
         * 最大重连次数
         */
        const val DEF_CONNECT_MAX_RETRY = 12
        /**
         * 重连间隔时间
         */
        const val DEF_CONNECT_RETRY_INTERVAL = 1000L

    }

    /**
     * 当前重连的次数
     */
    var attempt: Int
    /**
     * 重连最大次数
     */
    var connectMaxRetry: Int
    /**
     * 重连间隔
     */
    var connectRetryInterval: Long
    /**
     * 是否能进行重连
     */
    var isReconnecting: Boolean


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
    fun connectOrRetry(
        @NonNull bootstrap: Bootstrap,
        @NonNull host: String,
        port: Int,
        retry: Int
    )

    fun registerConnectOrRetryCallback(c: ConnectOrRetryCallback)

}

interface ConnectOrRetryCallback {
    /**
     * 链接或者重新成功
     */
    fun success(channel: Channel)

    /**
     * 链接或者重新失败
     */
    fun failure()

    /**
     * 开始重连
     */
    fun startReConnect(count:Int)

}