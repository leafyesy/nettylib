package com.example.nettylib.udp

import com.google.protobuf.MessageLite

class ProtoDataPacket(var address: String, var port: Int, var data: MessageLite)
