package com.example.nettylib.ssl

import android.content.Context
import io.netty.channel.ChannelPipeline
import io.netty.handler.ssl.SslHandler

/**
 * Created by leafye on 2019-10-26.

 */
class SecureChatSslHandler(private val context: Context) {
    /*
    "bksclient.keystore",
    "bksclient.keystore"
     */
    /**
     * @param pkFIle 加密验证文件
     * @param caFile 加密验证文件
     */
    fun secure(
        pkFIle: String,
        caFile: String,
        channelPipeline: ChannelPipeline
    ) {
        try {
            val engine = SecureChatSslContextFactory
                .getClientContextFromAssets(context, pkFIle, caFile)
                .createSSLEngine()
                .apply {
                    useClientMode = true
                }
            channelPipeline.addFirst("ssl", SslHandler(engine))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}