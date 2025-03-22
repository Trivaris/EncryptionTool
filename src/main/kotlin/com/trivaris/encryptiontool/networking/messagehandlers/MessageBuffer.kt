package com.trivaris.encryptiontool.networking.messagehandlers

import java.net.InetAddress

object MessageBuffer {
    private val buffer = mutableMapOf<InetAddress, MutableList<String>>()

    fun store(message: String, originator: InetAddress) =
        buffer
            .getOrPut(originator)
            { mutableListOf() }
            .add(message)

    fun consume(originator: InetAddress): MutableList<String>? =
        buffer.remove(originator)
}