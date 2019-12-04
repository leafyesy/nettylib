package com.example.nettylib.websocket.client

import android.content.ContentValues
import android.util.Log
import com.example.nettylib.handler.idog.SimpleWatchDog
import io.netty.bootstrap.Bootstrap
import java.util.concurrent.TimeUnit

/**
 * Created by leafye on 2019-12-05.
 */
class WebsocketWatchDogImp(val handler: WebSocketClientHandler) : SimpleWatchDog() {
    companion object {
        private val TAG = WebsocketWatchDogImp::class.java.simpleName
    }

    override fun connectOrRetry(bootstrap: Bootstrap, host: String, port: Int, retry: Int) {
        Log.d(TAG, "开始请求!!")
        attempt = connectMaxRetry - retry
        Log.e(ContentValues.TAG, "第${attempt}次重连, 间隔延迟: $connectRetryInterval 秒!")
        if (retry == 0) {
            Log.e(ContentValues.TAG, "重试次数已用完, 放弃连接!")
            // 关闭客户端, 释放资源
            // 连接服务器失败回调
            isReconnecting = false
            callback?.failure()
            return
        }
        try {
            bootstrap.connect(host, port).sync().channel().let {
                Log.d(TAG, "链接成功!!")
                if (handler.handshakeFuture() == null) {
                    reTry(retry, bootstrap, host, port)
                    return
                }
                handler.handshakeFuture()?.sync()
                Log.d(TAG, "handshakeFuture 成功!!")
                isReconnecting = false
                callback?.success(it)
            }
        } catch (e: Exception) {
            //链接失败 重连
            reTry(retry, bootstrap, host, port)
        }
    }

    private fun reTry(
        retry: Int,
        bootstrap: Bootstrap,
        host: String,
        port: Int
    ) {
        val order = connectMaxRetry - retry + 1
        val delay = connectRetryInterval
        bootstrap.config().group().schedule({
            try {
                callback?.startReConnect(order)
                connectOrRetry(bootstrap, host, port, retry - 1)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, delay, TimeUnit.SECONDS)
    }
}