package com.example.nettylib.httpFileServer

import io.netty.handler.codec.http.HttpRequest

interface RequestTransfer {

    fun redirect(httpRequest: HttpRequest): String

    fun getFilePath(httpRequest: HttpRequest): String

    fun onTransFinish(httpRequest: HttpRequest)

}