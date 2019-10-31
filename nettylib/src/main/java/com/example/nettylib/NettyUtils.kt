package com.example.nettylib

import java.net.NetworkInterface

/**
 * Created by leafye on 2019-10-26.
 */
class NettyUtils {

    companion object {
        /**
         * 获取路由广播地址 用于udp广播,使用该地址广播一般不会被路由器拦截
         */
        fun getRouteBroadcastAddress(): String {
            try {
                val networkInterfaces = NetworkInterface.getNetworkInterfaces()
                for (ni in networkInterfaces) {
                    if (!ni.isLoopback) {
                        val interfaceAddresses = ni.interfaceAddresses
                        for (interfaceAddr in interfaceAddresses) {
                            interfaceAddr.broadcast?.let {
                                return it.toString().substring(1)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return ""
        }
    }


}