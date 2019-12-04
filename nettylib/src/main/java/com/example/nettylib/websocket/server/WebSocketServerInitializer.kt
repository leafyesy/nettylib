package com.example.nettylib.websocket.server

import com.example.nettylib.decodeandencode.CustomMessageToMessageDecoder
import com.example.nettylib.decodeandencode.CustomMessageToMessageEncoder
import com.example.nettylib.tcp.server.ServerChannelOperator
import com.example.nettylib.websocket.benchmarkserver.WebSocketServerHandler
import com.google.protobuf.MessageLite
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.HttpServerCodec
import io.netty.handler.ssl.SslContext
import io.netty.handler.stream.ChunkedWriteHandler


/**
 * Created by leafye on 2019-11-28.
 */
class WebSocketServerInitializer(
    private val sslCtx: SslContext?,
    private val serverChannelOperator: ServerChannelOperator?,
    private val messageLite: MessageLite
) :
    ChannelInitializer<SocketChannel>() {

    @Throws(Exception::class)
    override fun initChannel(ch: SocketChannel) {
        val pipeline = ch.pipeline()
//        if (sslCtx != null) {
//            pipeline.addLast(sslCtx!!.newHandler(ch.alloc()))
//        }
        pipeline.addLast("http-codec", HttpServerCodec())
        pipeline.addLast("aggregator", HttpObjectAggregator(65536))
        pipeline.addLast("http-chunked", ChunkedWriteHandler())
        pipeline.addLast("decode", CustomMessageToMessageDecoder())
        pipeline.addLast("encode", CustomMessageToMessageEncoder())
        //pipeline.addLast("protobufdecode", ProtobufDecoder(messageLite))
        pipeline.addLast("handler", WebSocketServerHandler(messageLite))

//        pipeline.addLast(WebSocketServerCompressionHandler())
//        pipeline.addLast(WebSocketServerProtocolHandler(WEBSOCKET_PATH, null, true))
//        if (serverChannelOperator != null)
//            pipeline.addLast(ServerConnectionHandler(serverChannelOperator))
//        pipeline.addLast(WebSocketIndexPageHandler(WEBSOCKET_PATH))
//        pipeline.addLast(WebSocketFrameHandler())
    }

    companion object {

        private const val WEBSOCKET_PATH = "/websocket"
    }
}
