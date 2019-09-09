package com.example.nettylib.tcp

object Config {

    const val DATA_TYPE_HEART_BEAT: Byte = 0x00       //心跳数据类型
    const val DATA_TYPE_DATA: Byte = 0x01       //用户数据

    const val WRITE_IDLE_TIME = 5L      //心跳写间隔
    const val READ_IDLE_TIME = WRITE_IDLE_TIME * 3L      //心跳读超时时间

}
