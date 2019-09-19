package com.example.nettylib.file.client

import android.os.Environment
import android.util.Log
import io.netty.bootstrap.Bootstrap
import io.netty.channel.Channel
import io.netty.channel.EventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.http.*
import io.netty.handler.codec.http.multipart.*
import io.netty.util.CharsetUtil
import io.netty.util.internal.SocketUtils
import io.netty.util.internal.logging.InternalLoggerFactory
import io.netty.util.internal.logging.JdkLoggerFactory
import java.io.File
import java.io.FileNotFoundException
import java.net.URI

class HttpUploadClient : PushSuccessListener {

    companion object {
        init {
            DiskFileUpload.deleteOnExitTemporaryFile = true // should delete file on exit (in normal exit)
            DiskFileUpload.baseDirectory =
                Environment.getExternalStorageDirectory().absolutePath // system temp directory
            DiskAttribute.deleteOnExitTemporaryFile = true // should delete file on exit (in normal exit)
            DiskAttribute.baseDirectory =
                Environment.getExternalStorageDirectory().absolutePath // system temp directory
        }
    }

    private val ip: String
    private val port: Int
    private val file: File
    private var params: Map<String, String>?
    private var bodyRequestEncoder: HttpPostRequestEncoder? = null
    private val group: EventLoopGroup
    private var isSuc: Boolean = false
    private var channel: Channel? = null

    constructor(group: EventLoopGroup, ip: String, port: Int, file: File, params: Map<String, String>?) {
        this.ip = ip
        this.port = port
        this.file = file
        this.params = params
        this.group = group
        InternalLoggerFactory.setDefaultFactory(JdkLoggerFactory.INSTANCE)
    }

    fun isFinish(): Boolean {
        return if (channel != null) {
            channel?.isActive == false
        } else true
    }

    override fun success() {

    }

    fun getProgress(): Float {
        bodyRequestEncoder?.let {
            return (it.progress().toDouble() / it.length().toDouble()).toFloat()
        }
        return 0f
    }

    fun getFile(): File = file

    /**
     * 推送文件
     * @throws Exception
     */
    @Throws(Exception::class)
    fun pushFile() {
        isSuc = false
        val uri = URI("http://" + ip + ":" + port + "/" + file.name)
        if (!file.canRead()) {
            throw FileNotFoundException(file.absolutePath)
        }
        val factory = DefaultHttpDataFactory(true) // Disk if MINSIZE exceed
        val b = Bootstrap()
        b.group(group).channel(NioSocketChannel::class.java)
            .handler(HttpUploadClientInitializer(null, this))
        formPostMultipart(b, uri, factory, file, params)

    }

    /**
     * 打包并推送
     * @param bootstrap
     * @param uri
     * @param factory
     * @param file
     * @throws Exception
     */
    @Throws(Exception::class)
    private fun formPostMultipart(
        bootstrap: Bootstrap,
        uri: URI,
        factory: HttpDataFactory,
        file: File,
        params: Map<String, String>?
    ) {
        val future = bootstrap.connect(SocketUtils.socketAddress(uri.host, uri.port))
        // Wait until the connection attempt succeeds or fails.
        channel = future.sync().channel().apply ch@{
            // Prepare the HTTP request.
            var request: HttpRequest = DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, uri.toASCIIString())
            // Use the PostBody encoder
            bodyRequestEncoder = HttpPostRequestEncoder(
                factory,
                request,
                true,
                CharsetUtil.UTF_8,
                HttpPostRequestEncoder.EncoderMode.HTML5
            ) // true => multipart
            packageHeaders(request, uri)
            packageBodyList(bodyRequestEncoder, file, params)
            bodyRequestEncoder?.let { encoder ->
                request = encoder.finalizeRequest()
                this@ch.write(request)
                Log.d("upload", "length: " + encoder.length())
                if (encoder.isChunked) {
                    this@ch.write(bodyRequestEncoder)
                }
                this@ch.flush()
                encoder.cleanFiles()
                this@ch.closeFuture().addListener { factory.cleanAllHttpData() }
            }

        }
    }

    /**
     * 打包请求体
     * @param bodyRequestEncoder
     * @param file
     * @throws Exception
     */
    @Throws(Exception::class)
    private fun packageBodyList(
        bodyRequestEncoder: HttpPostRequestEncoder?,
        file: File,
        params: Map<String, String>?
    ) {
        bodyRequestEncoder?.addBodyFileUpload(
            "file",
            file,
            "application/x-zip-compressed",
            false
        )
    }


    /**
     * 打包请求头
     * @param request
     */
    private fun packageHeaders(request: HttpRequest, uri: URI) {
        request.headers().apply {
            set(HttpHeaderNames.HOST, uri.host)
            set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE)
            set(HttpHeaderNames.ACCEPT_ENCODING, HttpHeaderValues.GZIP.toString() + "," + HttpHeaderValues.DEFLATE)
            set(HttpHeaderNames.ACCEPT_CHARSET, "ISO-8859-1,utf-8;q=0.7,*;q=0.7")
            set(HttpHeaderNames.ACCEPT_LANGUAGE, "fr")
            set(HttpHeaderNames.REFERER, uri.toString())
            set(HttpHeaderNames.USER_AGENT, "Netty Simple Http Client side")
            set(HttpHeaderNames.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
            set(HttpHeaderValues.FILENAME, uri.rawPath)
            //            set(
            //                HttpHeaderNames.COOKIE, ClientCookieEncoder.STRICT.encode(
            //                        new DefaultCookie("my-cookie", "foo"),
            //                        new DefaultCookie("another-cookie", "bar"))
            //        );
        }
    }
}

