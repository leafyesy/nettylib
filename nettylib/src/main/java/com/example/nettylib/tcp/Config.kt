package com.example.nettylib.tcp

object Config {

    val DATA_TYPE_HEART_BEAT: Byte = 0x00       //心跳数据类型
    val DATA_TYPE_DATA: Byte = 0x01       //用户数据

    var WRITE_IDLE_TIME = 5L      //心跳写间隔
    var READ_IDLE_TIME = WRITE_IDLE_TIME * 3L      //心跳读超时时间

}
