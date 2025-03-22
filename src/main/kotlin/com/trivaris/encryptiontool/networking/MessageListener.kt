package com.trivaris.encryptiontool.networking

import java.net.InetAddress

interface MessageListener {
    fun onMessageReceived(message: String, originator: InetAddress)
}