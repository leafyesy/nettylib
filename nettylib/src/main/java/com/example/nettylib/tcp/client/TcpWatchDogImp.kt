package com.example.nettylib.tcp.client

import android.content.ContentValues
import android.util.Log
import com.example.nettylib.handler.idog.SimpleWatchDog
import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelFuture
import java.util.concurrent.TimeUnit

/**
 * Created by leafye on 2019-12-05.
 */
class TcpWatchDogImp : SimpleWatchDog() {

    override fun connectOrRetry(bootstrap: Bootstrap, host: String, port: Int, retry: Int) {
        attempt = connectMaxRetry - retry
        Log.e(ContentValues.TAG, "第${attempt}次重连, 间隔延迟: $connectRetryInterval 秒!")
        bootstrap.connect(host, port).addListener { future ->
            if (future is ChannelFuture)
                when {
                    future.isSuccess -> {
                        // 客户端连接渠道
                        Log.d(ContentValues.TAG, "连接成功!")
                        isReconnecting = false
                        future.channel()?.apply {
                            callback?.success(this)
                        }
                    }
                    retry == 0 -> {
                        Log.e(ContentValues.TAG, "重试次数已用完, 放弃连接!")
                        // 关闭客户端, 释放资源
                        // 连接服务器失败回调
                        isReconnecting = false
                        callback?.failure()
                    }
                    else -> {
                        // 第几次重连
                        val order = connectMaxRetry - retry + 1
                        // 本次重连的间隔
                        val delay = connectRetryInterval
                        Log.e(
                            ContentValues.TAG,
                            "连接失败, 第" + order + "次重连, " + "间隔延迟: " + delay + "秒!"
                        )
                        callback?.startReConnect(order)
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