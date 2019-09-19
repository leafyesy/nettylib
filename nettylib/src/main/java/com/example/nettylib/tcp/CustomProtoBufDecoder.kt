package com.example.nettylib.tcp

import com.example.nettylib.proto.HeartBeatData
import com.google.protobuf.MessageLite
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import kotlin.experimental.and

class CustomProtoBufDecoder(private val prototype: MessageLite) : ByteToMessageDecoder() {

    @Throws(Exception::class)
    override fun decode(ctx: ChannelHandlerContext, `in`: ByteBuf, out: MutableList<Any>) {
        while (`in`.readableBytes() > 4) { // 如果可读长度小于包头长度，退出。
            `in`.markReaderIndex()
            // 获取包头中的body长度
            val low = `in`.readByte()
            val high = `in`.readByte()
            val s0 = (low and 0xff.toByte()).toInt()
            var s1 = (high and 0xff.toByte()).toInt()
            s1 = s1.shl(8)
            val length = (s0 or s1).toShort()
            // 获取包头中的protobuf类型
            `in`.readByte()
            val dataType = `in`.readByte()
            // 如果可读长度小于body长度，恢复读指针，退出。
            if (`in`.readableBytes() < length) {
                `in`.resetReaderIndex()
                return
            }
            // 读取body
            val bodyByteBuf = `in`.readBytes(length.toInt())

            val array: ByteArray
            val offset: Int

            val readableLen = bodyByteBuf.readableBytes()
            if (bodyByteBuf.hasArray()) {
                array = bodyByteBuf.array()
                offset = bodyByteBuf.arrayOffset() + bodyByteBuf.readerIndex()
            } else {
                array = ByteArray(readableLen)
                bodyByteBuf.getBytes(bodyByteBuf.readerIndex(), array, 0, readableLen)
                offset = 0
            }

            //反序列化
            val result = decodeBody(dataType, array, offset, readableLen)
            result?.let {
                out.add(result)
            }
        }
    }

    @Throws(Exception::class)
    fun decodeBody(dataType: Byte, array: ByteArray, offset: Int, length: Int): MessageLite? {
        if (dataType == Config.DATA_TYPE_HEART_BEAT) {
            return HeartBeatData.HeartBeat.getDefaultInstance().parserForType.parseFrom(array, offset, length)
        } else if (dataType == Config.DATA_TYPE_DATA) {
            return prototype.parserForType.parseFrom(array, offset, length)
        }
        return null // or throw exception
    }
}