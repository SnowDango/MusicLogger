package com.snowdango.musiclogger.repository.api

import java.net.InetAddress
import java.net.Socket
import javax.net.SocketFactory

class CustomSocketFactory : SocketFactory() {

    override fun createSocket(): Socket {
        val socket = Socket()
        socket.tcpNoDelay = true
        return socket
    }

    override fun createSocket(host: String?, port: Int): Socket {
        val socket = Socket(host, port)
        socket.tcpNoDelay = true
        return socket
    }

    override fun createSocket(host: String?, port: Int, localHost: InetAddress?, localPort: Int): Socket {
        val socket = Socket(host, port, localHost, localPort)
        socket.tcpNoDelay = true
        return socket
    }

    override fun createSocket(host: InetAddress?, port: Int): Socket {
        val socket = Socket(host, port)
        socket.tcpNoDelay = true
        return socket
    }

    override fun createSocket(address: InetAddress?, port: Int, localAddress: InetAddress?, localPort: Int): Socket {
        val socket = Socket(address, port, localAddress, localPort)
        socket.tcpNoDelay = true
        return socket
    }

}