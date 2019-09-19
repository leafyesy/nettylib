package com.example.nettylib.tcp

import com.example.nettylib.proto.HeartBeatData
import com.google.protobuf.MessageLite
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder

/**
 * 参考ProtobufVarint32LengthFieldPrepender 和 ProtobufEncoder
 */
@ChannelHandler.Sharable
class CustomProtoBufEncoder : MessageToByteEncoder<MessageLite>() {

    @Throws(Exception::class)
    override fun encode(ctx: ChannelHandlerContext, msg: MessageLite, out: ByteBuf) {


        val body = msg.toByteArray()
        val header = encodeHeader(msg, body.size)

        out.writeBytes(header)
        out.writeBytes(body)
    }

    private fun encodeHeader(msg: MessageLite, bodyLength: Int): ByteArray {
        val messageType: Byte = if (msg is HeartBeatData.HeartBeat) {
            //心跳
            Config.DATA_TYPE_HEART_BEAT
        } else {
            Config.DATA_TYPE_DATA
        }
        val header = ByteArray(4)
        header[0] = (bodyLength and 0xff).toByte()
        header[1] = (bodyLength.shr(8) and 0xff).toByte()
        header[2] = 0 // 保留字段
        header[3] = messageType
        return header

    }
}
