package com.example.nettylib.file.server

import java.io.File
import java.net.URI

interface FileReceiveListener {
    /**
     * 获取存储的文件
     */
    fun getSaveFile(uri: URI, params: Map<String, String>): File

    /**
     *
     */
    fun onStartReceiveFile(uri: URI, params: Map<String, String>)

    /**
     *
     */
    fun onFinishReceiveFile(file: File, params: Map<String, String>)

}