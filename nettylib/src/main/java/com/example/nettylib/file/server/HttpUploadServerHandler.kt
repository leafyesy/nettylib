package com.example.nettylib.file.server

import android.os.Environment
import android.text.TextUtils
import android.util.Log
import io.netty.buffer.Unpooled.copiedBuffer
import io.netty.channel.Channel
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.*
import io.netty.handler.codec.http.cookie.Cookie
import io.netty.handler.codec.http.cookie.ServerCookieDecoder
import io.netty.handler.codec.http.cookie.ServerCookieEncoder
import io.netty.handler.codec.http.multipart.*
import io.netty.util.CharsetUtil
import java.net.URI
import java.util.HashMap

class HttpUploadServerHandler
    (private var fileReceiveListener: FileReceiveListener?) : SimpleChannelInboundHandler<HttpObject>() {


    companion object {
        private val factory = DefaultHttpDataFactory(true) // Disk if size exceed
    }

    private var decoder: HttpPostRequestDecoder? = null
    private var request: HttpRequest? = null
    private val responseContent = StringBuilder()
    private var uri: URI? = null
    private val params = HashMap<String, String>()

    init {
        DiskFileUpload.deleteOnExitTemporaryFile = false
        DiskFileUpload.baseDirectory =
            Environment.getExternalStorageDirectory().path + "/Download/"
        DiskAttribute.deleteOnExitTemporaryFile = false
        DiskAttribute.baseDirectory =
            Environment.getExternalStorageDirectory().path + "/Download/"
    }

    override fun channelRead0(ctx: ChannelHandlerContext?, msg: HttpObject?) {
        if (msg is HttpRequest) {
            this.request = msg
            request?.let {
                uri = URI(it.uri())
                // if GET Method: should not try to create a HttpPostRequestDecoder
                if (HttpMethod.GET == it.method()) {
                    // GET Method: should not try to create a HttpPostRequestDecoder
                    // So stop here
                    // Not now: LastHttpContent will be sent writeResponse(ctx.channel());
                    return
                }
            }
            try {
                decoder = HttpPostRequestDecoder(factory, request, CharsetUtil.UTF_8)
            } catch (e1: HttpPostRequestDecoder.ErrorDataDecoderException) {
                e1.printStackTrace()
                responseContent.append(e1.message)
                ctx?.let {
                    writeResponse(ctx.channel(), true)
                }
                return
            }
            uri?.let {
                fileReceiveListener?.onStartReceiveFile(it, params)
            }
        }
        if (decoder != null) {
            if (msg is HttpContent) {
                try {
                    decoder?.offer(msg)
                } catch (e1: HttpPostRequestDecoder.ErrorDataDecoderException) {
                    e1.printStackTrace()
                    ctx?.let {
                        writeResponse(ctx.channel(), true)
                    }
                    return
                }
                readHttpDataChunkByChunk()
                if (msg is LastHttpContent) {
                    ctx?.let {
                        writeResponse(it.channel())
                    }
                    reset()
                }
            }
        } else {
            ctx?.let {
                writeResponse(ctx.channel())
            }
        }
    }

    private fun reset() {
        request = null
        decoder?.destroy()
        decoder = null
        uri = null
    }

    private fun writeResponse(channel: Channel) {
        writeResponse(channel, false)
    }

    private fun writeResponse(channel: Channel, forceClose: Boolean) {
        // Convert the response content to a ChannelBuffer.
        val buf = copiedBuffer(responseContent.toString(), CharsetUtil.UTF_8)
        responseContent.setLength(0)

        // Decide whether to close the connection or not.
        val keepAlive = HttpUtil.isKeepAlive(request) && !forceClose

        // Build the response object.
        val response = DefaultFullHttpResponse(
            HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf
        )
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8")
        response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, buf.readableBytes())

        if (!keepAlive) {
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE)
        } else if (request?.protocolVersion() == HttpVersion.HTTP_1_0) {
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE)
        }

        val cookies: Set<Cookie>
        val value = request?.headers()?.get(HttpHeaderNames.COOKIE)
        if (value == null) {
            cookies = emptySet()
        } else {
            cookies = ServerCookieDecoder.STRICT.decode(value)
        }
        if (!cookies.isEmpty()) {
            // Reset the cookies if necessary.
            for (cookie in cookies) {
                response.headers().add(HttpHeaderNames.SET_COOKIE, ServerCookieEncoder.STRICT.encode(cookie))
            }
        }
        // Write the response.
        val future = channel.writeAndFlush(response)
        // Close the connection after the write operation is done if necessary.
        if (!keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE)
        }
    }

    @Throws(Exception::class)
    private fun readHttpDataChunkByChunk() {
        try {
            while (decoder?.hasNext() == true) {
                decoder?.next()?.let {
                    writeHttpData(it)
                }
            }
        } catch (e1: HttpPostRequestDecoder.EndOfDataDecoderException) {
            e1.printStackTrace()
        }
    }

    @Throws(Exception::class)
    private fun writeHttpData(data: InterfaceHttpData) {
        if (data.httpDataType == InterfaceHttpData.HttpDataType.Attribute) {
            //TODO 在mutipart模式下取不到value
            val attribute = data as Attribute
            var name = attribute.name
            val pairs = name.split("&&&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            name = pairs[0]
            var value = pairs[1]
            if (!TextUtils.isEmpty(attribute.value)) {
                value = attribute.value
            }
            params[name] = value
        } else {
            if (data.httpDataType == InterfaceHttpData.HttpDataType.FileUpload) {
                Log.d("upload", "fileUpload===")
                val fileUpload = data as FileUpload
                if (fileUpload.isCompleted) {
                    if (fileReceiveListener != null) {
                        val file = fileUpload.file
                        Log.d("upload", file.absolutePath + "------------")
                    }
                    decoder?.removeHttpDataFromClean(fileUpload)
                }
            }
        }
    }

    @Throws(Exception::class)
    override fun channelInactive(ctx: ChannelHandlerContext) {
        decoder?.cleanFiles()
    }

    @Throws(Exception::class)
    override fun channelActive(ctx: ChannelHandlerContext) {

    }

    @Throws(Exception::class)
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        Log.e("err", cause.message)
        ctx.channel().close()
    }


}