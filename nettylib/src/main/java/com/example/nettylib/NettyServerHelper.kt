package com.example.nettylib

import android.content.Context
import android.net.wifi.WifiManager
import android.util.Log
import com.example.nettylib.proto.HeartBeatData
import com.example.nettylib.ssl.SecureChatSslContextFactory
import com.example.nettylib.tcp.server.TcpProtobufServer
import com.example.nettylib.udp.UdpHandler
import com.google.protobuf.MessageLite
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPipeline
import io.netty.handler.ssl.SslHandler
import java.lang.Exception
import java.util.concurrent.Executors

class NettyServerHelper(private val context: Context) {

    companion object {
        private val TAG = NettyServerHelper::class.java.simpleName
    }

    private var broadcastServer = Executors.newSingleThreadScheduledExecutor()
    private var serverExecutor = Executors.newScheduledThreadPool(1)


    private val udpHandler by lazy {
        object : UdpHandler() {
            /**
             * 广播IP
             */
            fun broadcastIP() {
                Log.d("udp", System.currentTimeMillis().toString() + "-")
                broadcast(
                    System.currentTimeMillis().toString() + "",
                    Constant.UDP_SERVICE_PLATE_PORT,
                    isWifiOpened()
                )
            }

            private fun isWifiOpened(): Boolean {
                val wifiManager =
                    context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                return wifiManager.isWifiEnabled
            }
        }
    }

    private val tcpProtoBufServer by lazy {
        object : TcpProtobufServer() {
            override fun onReadData(channelHandlerContext: ChannelHandlerContext, messageLite: MessageLite) {
                super.onReadData(channelHandlerContext, messageLite)
                Log.e(TAG, "服务端接收到数据!!!")
            }

            override fun handleChannelPipeline(channelPipeline: ChannelPipeline) {
                super.handleChannelPipeline(channelPipeline)
                try {
                    val engine = SecureChatSslContextFactory
                        .getServerContextFromAssets(context, "bksserver.keystore", "bksserver.keystore")
                        .createSSLEngine()
                    engine.useClientMode = false//设置服务端模式
                    engine.needClientAuth = true//需要客户端验证
                    channelPipeline.addFirst("ssl", SslHandler(engine))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onChannelConnect(channelHandlerContext: ChannelHandlerContext) {
                super.onChannelConnect(channelHandlerContext)
                Log.e(TAG, "服务端连接!!!")
            }

            override fun onChannelDisconnect(channelHandlerContext: ChannelHandlerContext) {
                super.onChannelDisconnect(channelHandlerContext)
                Log.e(TAG, "服务端断开连接!!!")
            }
        }
    }


    fun bind() {
        Log.d(TAG, "start server bind!!!")
        serverExecutor.execute {
            try {
                udpHandler.bind(Constant.UDP_SERVICE_CONTROLLER_PORT)
                tcpProtoBufServer.bind(Constant.TCP_SERVICE_PORT, HeartBeatData.HeartBeat.getDefaultInstance())
                    .addListener {
                        if (it.isSuccess) {
                            Log.e(TAG, "tcp 消息通道创建成功!!!")
                            handleTcpConnectionSuccess()
                        }
                    }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun handleTcpConnectionSuccess() {
        udpHandler.broadcastIP()
    }

    fun shutdown() {
        try {
            tcpProtoBufServer.shutdown()
            udpHandler.stop()
            serverExecutor.shutdown()
            broadcastServer.shutdown()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}