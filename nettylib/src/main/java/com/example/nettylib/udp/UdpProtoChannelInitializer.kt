package com.example.nettylib.udp

import com.google.protobuf.MessageLite
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.DatagramChannel
import io.netty.handler.codec.protobuf.ProtobufDecoder

class UdpProtoChannelInitializer(messageLite: MessageLite, protoDataOperator: ProtoDataOperator) :
    ChannelInitializer<DatagramChannel>() {

    private var messageLite: MessageLite? = messageLite
    private var protoDataOperator: ProtoDataOperator? = protoDataOperator

    @Throws(Exception::class)
    override fun initChannel(ch: DatagramChannel) {
        val channelPipeline = ch.pipeline()
            .addLast(DataPacketProtoEncoder())
            .addLast(DataPacketProtoDecoder())
        //.addLast(new ProtobufVarint32LengthFieldPrepender())
        //.addLast(new ProtobufVarint32FrameDecoder())
        if (messageLite != null)
            channelPipeline.addLast(ProtobufDecoder(messageLite))
        if (protoDataOperator != null)
            channelPipeline.addLast(ProtoDataHandler(protoDataOperator))
    }
}