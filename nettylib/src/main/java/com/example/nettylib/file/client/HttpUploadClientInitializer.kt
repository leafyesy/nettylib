package com.example.nettylib.file.client

import com.example.nettylib.file.HttpUploadClientHandler
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.http.HttpClientCodec
import io.netty.handler.ssl.SslContext
import io.netty.handler.stream.ChunkedWriteHandler

class HttpUploadClientInitializer(private var sslCtx: SslContext?, pushSucListener: PushSuccessListener) :
    ChannelInitializer<SocketChannel>() {

    private var pushSucListener: PushSuccessListener? = pushSucListener

    override fun initChannel(ch: SocketChannel) {
        val pipeline = ch.pipeline()
        sslCtx?.let {
            pipeline.addLast(it.newHandler(ch.alloc()))
        }
        pipeline.addLast(HttpClientCodec())
        pipeline.addLast(ChunkedWriteHandler())
        pushSucListener?.let {
            pipeline.addLast(HttpUploadClientHandler().apply {
                setPushSucListener(it)
            })
        }
    }
}