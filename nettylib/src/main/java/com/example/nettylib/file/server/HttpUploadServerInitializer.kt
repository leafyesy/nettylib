package com.example.nettylib.file.server

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.http.HttpRequestDecoder
import io.netty.handler.codec.http.HttpResponseEncoder
import io.netty.handler.ssl.SslContext

class HttpUploadServerInitializer(
    sslCtx: SslContext,
    fileReceiveListener: FileReceiveListener
) :
    ChannelInitializer<SocketChannel>() {

    private var sslCtx: SslContext? = sslCtx
    private var fileReceiveListener: FileReceiveListener? = fileReceiveListener

    public override fun initChannel(ch: SocketChannel) {
        val pipeline = ch.pipeline()

        sslCtx?.let {
            pipeline.addLast(it.newHandler(ch.alloc()))
        }

        pipeline.addLast(HttpRequestDecoder())
        pipeline.addLast(HttpResponseEncoder())
        pipeline.addLast(HttpUploadServerHandler(fileReceiveListener))
    }
}