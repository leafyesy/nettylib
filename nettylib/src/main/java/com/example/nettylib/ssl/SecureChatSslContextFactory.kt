package com.example.nettylib.ssl

import android.content.Context
import java.io.IOException
import java.io.InputStream
import java.security.KeyStore
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory

object SecureChatSslContextFactory {

    private const val PROTOCOL = "TLS"

    private var SERVER_CONTEXT: SSLContext? = null//服务器安全套接字协议

    private var CLIENT_CONTEXT: SSLContext? = null//客户端安全套接字协议


    fun getServerContextFromAssets(context: Context, pkFile: String?, caFile: String?): SSLContext {
        if (SERVER_CONTEXT != null) return SERVER_CONTEXT!!
        var `in`: InputStream? = null
        var tIN: InputStream? = null
        try {
            //密钥管理器
            var kmf: KeyManagerFactory? = null
            if (pkFile != null) {
                val ks = KeyStore.getInstance("BKS")
                //                in = new FileInputStream(pkPath);
                `in` = context.assets.open(pkFile)
                ks.load(`in`, "123456".toCharArray())

                kmf = KeyManagerFactory.getInstance("X509")
                kmf!!.init(ks, "123456".toCharArray())
            }
            //信任库
            var tf: TrustManagerFactory? = null
            if (caFile != null) {
                val tks = KeyStore.getInstance("BKS")
                //                tIN = new FileInputStream(caPath);
                tIN = context.assets.open(caFile)
                tks.load(tIN, "123456".toCharArray())
                tf = TrustManagerFactory.getInstance("X509")
                tf!!.init(tks)
            }

            SERVER_CONTEXT = SSLContext.getInstance(PROTOCOL)
            //初始化此上下文
            //参数一：认证的密钥      参数二：对等信任认证  参数三：伪随机数生成器 。 由于单向认证，服务端不用验证客户端，所以第二个参数为null
            SERVER_CONTEXT!!.init(kmf!!.keyManagers, tf!!.trustManagers, null)

        } catch (e: Exception) {
            throw Error("Failed to initialize the server-side SSLContext", e)
        } finally {
            if (`in` != null) {
                try {
                    `in`.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                `in` = null
            }
            if (tIN != null) {
                try {
                    tIN.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                tIN = null
            }
        }
        return SERVER_CONTEXT!!
    }


    fun getClientContextFromAssets(context: Context, pkFile: String?, caFile: String?): SSLContext {
        if (CLIENT_CONTEXT != null) return CLIENT_CONTEXT!!
        synchronized(PROTOCOL) {
            var `in`: InputStream? = null
            var tIN: InputStream? = null
            try {
                var kmf: KeyManagerFactory? = null
                if (pkFile != null) {
                    val ks = KeyStore.getInstance("BKS")
                    //                in = new FileInputStream(pkPath);
                    `in` = context.assets.open(pkFile)
                    ks.load(`in`, "123456".toCharArray())
                    kmf = KeyManagerFactory.getInstance("X509")
                    kmf!!.init(ks, "123456".toCharArray())
                }
                var tf: TrustManagerFactory? = null
                if (caFile != null) {
                    val tks = KeyStore.getInstance("BKS")
                    //                tIN = new FileInputStream(caPath);
                    tIN = context.assets.open(caFile)
                    tks.load(tIN, "123456".toCharArray())
                    tf = TrustManagerFactory.getInstance("X509")
                    tf!!.init(tks)
                }
                CLIENT_CONTEXT = SSLContext.getInstance(PROTOCOL)
                //初始化此上下文
                //参数一：认证的密钥      参数二：对等信任认证  参数三：伪随机数生成器 。 由于单向认证，服务端不用验证客户端，所以第二个参数为null
                CLIENT_CONTEXT!!.init(kmf!!.keyManagers, tf!!.trustManagers, null)

            } catch (e: Exception) {
                e.printStackTrace()
                throw Error("Failed to initialize the client-side SSLContext")
            } finally {
                if (`in` != null) {
                    try {
                        `in`.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    `in` = null
                }
                if (tIN != null) {
                    try {
                        tIN.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    tIN = null
                }
            }
        }
        return CLIENT_CONTEXT!!
    }

}