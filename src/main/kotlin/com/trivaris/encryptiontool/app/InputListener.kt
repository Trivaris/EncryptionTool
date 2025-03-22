package com.trivaris.encryptiontool.app

import java.net.InetAddress

interface InputListener {
    fun onSendButtonClicked(message: String, recipient: InetAddress)
}