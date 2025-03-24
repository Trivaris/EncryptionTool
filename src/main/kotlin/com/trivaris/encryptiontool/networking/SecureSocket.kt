package com.trivaris.encryptiontool.networking

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.io.readBytes
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket

class SecureSocket(private val port: Int) {
    private var serverSocket: ServerSocket = ServerSocket(port)
    private var listeners = mutableListOf<MessageListener>()

    @Volatile
    private var isServerRunning = false
    private val scope = CoroutineScope(Dispatchers.IO)

    fun addListener(listener: MessageListener) =
        listeners.add(listener)

    fun startServer() {
        synchronized(this) {
            if (isServerRunning) {
                println("Server is already running.")
                return
            }
            isServerRunning = true
        }

        println("Starting server on port $port...")
        scope.launch {
            try {
                while (isServerRunning) {
                    val client = serverSocket.accept()
                    val address = client.inetAddress
                    val input = client.inputStream
                    val message = input.readBytes().decodeToString()

                    println("Received: $message")

                    listeners.forEach { it.onMessageReceived(message, address) }
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    fun sendMessage(address: InetAddress, message: String, port: Int = getPort()) {
        scope.launch {
            val socket = Socket(address, port)
            val output = socket.getOutputStream()
            println("Sending: $message")
            output.write(message.encodeToByteArray())
            output.flush()
            socket.close()
        }
    }

    fun getPort(): Int = port

    init {
        startServer()
    }
}