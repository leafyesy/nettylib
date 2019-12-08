package com.example.nettylib.handler.idog

import io.netty.bootstrap.Bootstrap

/**
 * Created by leafye on 2019-12-05.
 */
open class SimpleWatchDog(
    override var attempt: Int = 0,
    override var connectMaxRetry: Int = IWatchDog.DEF_CONNECT_MAX_RETRY,
    override var connectRetryInterval: Long = IWatchDog.DEF_CONNECT_RETRY_INTERVAL,
    override var isReconnecting: Boolean = false
) : IWatchDog {

    protected var callback: ConnectOrRetryCallback? = null

    override fun registerConnectOrRetryCallback(c: ConnectOrRetryCallback) {
        this.callback = c
    }

    override fun connectOrRetry(bootstrap: Bootstrap, host: String, port: Int, retry: Int) {

    }

}