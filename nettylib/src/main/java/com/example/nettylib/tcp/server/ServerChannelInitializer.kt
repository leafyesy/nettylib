package com.example.nettylib.tcp.server

import com.example.nettylib.ConnectorIdleStateTrigger
import com.example.nettylib.tcp.Config
import com.example.nettylib.tcp.CustomProtobufDecoder
import com.example.nettylib.tcp.CustomProtobufEncoder
import com.google.protobuf.MessageLite
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.timeout.IdleStateHandler
import java.util.concurrent.TimeUnit

class ServerChannelInitializer(
    private val messageLite: MessageLite,
    private var serverChannelOperator: ServerChannelOperator
) :
    ChannelInitializer<SocketChannel>() {

    @Throws(Exception::class)
    override fun initChannel(socketChannel: SocketChannel) {
        val channelPipeline = socketChannel.pipeline()
            .addLast(CustomProtobufEncoder())
            .addLast(IdleStateHandler(Config.READ_IDLE_TIME, Config.WRITE_IDLE_TIME, 0, TimeUnit.SECONDS))
            .addLast(ConnectorIdleStateTrigger())
            .addLast(ServerConnectionHandler(serverChannelOperator))
            .addLast(CustomProtobufDecoder(messageLite))
            .addLast(ServerDataHandler(serverChannelOperator))
        serverChannelOperator.handleChannelPipeline(channelPipeline)
    }
}