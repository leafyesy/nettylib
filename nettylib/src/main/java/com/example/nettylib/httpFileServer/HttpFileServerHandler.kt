package com.example.nettylib.httpFileServer

import android.text.TextUtils
import android.util.Log
import io.netty.buffer.Unpooled
import io.netty.channel.*
import io.netty.handler.codec.http.*
import io.netty.handler.stream.ChunkedFile
import io.netty.util.CharsetUtil
import java.io.File
import java.io.FileNotFoundException
import java.io.RandomAccessFile
import java.util.regex.Pattern

class HttpFileServerHandler(requestTransfer: RequestTransfer) : SimpleChannelInboundHandler<FullHttpRequest>() {

    companion object {
        private val ALLOWED_FILE_NAME = Pattern.compile("[A-Za-z0-9][-_A-Za-z0-9\\.]*")
    }

    private var requestTransfer: RequestTransfer? = requestTransfer

    @Throws(Exception::class)
    override fun channelActive(ctx: ChannelHandlerContext) {
        super.channelActive(ctx)
        Log.d("fileserver", "active")
    }

    @Throws(Exception::class)
    override fun channelInactive(ctx: ChannelHandlerContext) {
        super.channelInactive(ctx)
        Log.d("fileserver", "inactive")
    }

    @Throws(Exception::class)
    override fun channelRead0(
        ctx: ChannelHandlerContext,
        request: FullHttpRequest
    ) {
        Log.d("fileserver", "recevedata" + request.uri())
        if (!request.decoderResult().isSuccess) {
            sendError(ctx, HttpResponseStatus.BAD_REQUEST)
            return
        }
        if (request.method() !== HttpMethod.GET) {
            sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED)
            return
        }
        var path: String? = null
        requestTransfer?.let {
            val redirectHost = it.redirect(request)
            if (!TextUtils.isEmpty(redirectHost)) {
                sendRedirect(ctx, redirectHost)
                return
            }
            path = it.getFilePath(request)
        }
        if (path == null) {
            sendError(ctx, HttpResponseStatus.NOT_FOUND)
            return
        }
        val file = File(path)
        if (file.isHidden || !file.exists()) {
            sendError(ctx, HttpResponseStatus.NOT_FOUND)
            return
        }
        if (!file.isFile) {
            sendError(ctx, HttpResponseStatus.FORBIDDEN)
            return
        }
        var randomAccessFile: RandomAccessFile? = null
        try {
            randomAccessFile = RandomAccessFile(file, "r")
        } catch (fnfd: FileNotFoundException) {
            sendError(ctx, HttpResponseStatus.NOT_FOUND)
            return
        }
        val fileLength = randomAccessFile.length()
        val response = DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK)
        setContentTypeHeader(response, file)
        response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE)

        ctx.write(response)
        var sendFileFuture: ChannelFuture? = null
        sendFileFuture = ctx.write(ChunkedFile(randomAccessFile, 0, fileLength, 8192), ctx.newProgressivePromise())
        sendFileFuture!!.addListener(object : ChannelProgressiveFutureListener {

            @Throws(Exception::class)
            override fun operationComplete(future: ChannelProgressiveFuture) {
                requestTransfer?.onTransFinish(request)
            }

            @Throws(Exception::class)
            override fun operationProgressed(
                future: ChannelProgressiveFuture,
                progress: Long, total: Long
            ) {
                //                if(total < 0)
                //                    System.err.println("Transfer progress: " + progress);
                //                else
                //                    System.err.println("Transfer progress: " + progress + "/" + total);
            }
        })

        val lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT)
        //        if(!HttpHeaderUtil.isKeepAlive(request))
        lastContentFuture.addListener(ChannelFutureListener.CLOSE)

    }

    @Throws(Exception::class)
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        if (ctx.channel().isActive)
            sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR)
    }

    private val INSECURE_URI = Pattern.compile(".*[<>&\"].*")
//    private String sanitizeUri(String uri) throws Exception{
//        URI URI = new URI(uri);
//        Log.d("fileserver",URI.getPath());
//
//        if (requestTransfer != null)
//            return requestTransfer.getFilePath();
//
//        return null;
//        if (URI.getPath().startsWith("/helloword"))
//            return Environment.getExternalStorageDirectory().getAbsolutePath() + "/hello/index.m3u8";
//        else
//            return Environment.getExternalStorageDirectory().getAbsolutePath() + "/hello" + URI.getPath();
//    }


    private fun sendListing(ctx: ChannelHandlerContext, dir: File) {
        val response = DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK)
        //        response.headers().set("CONNECT_TYPE", "text/html;charset=UTF-8");
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8")

        val dirPath = dir.path
        val buf = StringBuilder()

        buf.append("<!DOCTYPE html>\r\n")
        buf.append("<html><head><title>")
        buf.append(dirPath)
        buf.append("目录:")
        buf.append("</title></head><body>\r\n")

        buf.append("<h3>")
        buf.append(dirPath).append(" 目录：")
        buf.append("</h3>\r\n")
        buf.append("<ul>")
        buf.append("<li>链接：<a href=\" ../\")..</a></li>\r\n")
        for (f in dir.listFiles()) {
            if (f.isHidden || !f.canRead()) {
                continue
            }
            val name = f.name
            if (!ALLOWED_FILE_NAME.matcher(name).matches()) {
                continue
            }

            buf.append("<li>链接：<a href=\"")
            buf.append(name)
            buf.append("\">")
            buf.append(name)
            buf.append("</a></li>\r\n")
        }

        buf.append("</ul></body></html>\r\n")

        val buffer = Unpooled.copiedBuffer(buf, CharsetUtil.UTF_8)
        response.content().writeBytes(buffer)
        buffer.release()
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE)
    }


    private fun sendRedirect(ctx: ChannelHandlerContext, newUri: String) {
        val response = DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FOUND)
        //        response.headers().set("LOCATIN", newUri);
        response.headers().set(HttpHeaderNames.LOCATION, newUri)
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE)
    }

    private fun sendError(ctx: ChannelHandlerContext, status: HttpResponseStatus) {
        val response = DefaultFullHttpResponse(
            HttpVersion.HTTP_1_1, status,
            Unpooled.copiedBuffer("Failure: $status\r\n", CharsetUtil.UTF_8)
        )
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8")
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE)
    }

    private fun setContentTypeHeader(response: HttpResponse, file: File) {
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/octet-stream")
    }
}