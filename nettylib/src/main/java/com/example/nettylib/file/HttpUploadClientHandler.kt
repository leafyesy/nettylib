package com.example.nettylib.file

import android.util.Log
import com.example.nettylib.file.client.PushSuccessListener
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.HttpObject
import io.netty.handler.codec.http.HttpResponse

open class HttpUploadClientHandler : SimpleChannelInboundHandler<HttpObject>() {

    private val readingChunks: Boolean = false
    private var pushSucListener: PushSuccessListener? = null

    fun setPushSucListener(l: PushSuccessListener) {
        this.pushSucListener = l
    }

    override fun channelRead0(p0: ChannelHandlerContext?, msg: HttpObject?) {
        Log.d("upload", "------------")
        if (msg is HttpResponse) {
            val response = msg as HttpResponse
            Log.d("upload", "STATUS: " + response.status())
            Log.d("upload", "VERSION: " + response.protocolVersion())
            if (response.status().code() == 200 && pushSucListener != null) {
                pushSucListener?.success()
            }
        }
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        ctx.channel().close()
    }
}