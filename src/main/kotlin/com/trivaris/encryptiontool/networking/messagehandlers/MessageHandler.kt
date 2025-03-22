package com.trivaris.encryptiontool.networking.messagehandlers

import com.trivaris.encryptiontool.app.UserInterface
import com.trivaris.encryptiontool.encryption.EncryptionStrategy
import com.trivaris.encryptiontool.keymanagement.KeyGenerator
import com.trivaris.encryptiontool.keymanagement.KeyManager
import com.trivaris.encryptiontool.networking.MessageListener
import com.trivaris.encryptiontool.networking.SecureSocket
import java.net.InetAddress

class MessageHandler(
    private val socket: SecureSocket,
    private val ui: UserInterface,
    private val encryptionStrategy: EncryptionStrategy
) : MessageListener {

    override fun onMessageReceived(message: String, originator: InetAddress) {
        val type = MessageType.fromRaw(message)

        when (type) {

            MessageType.PLAIN -> {
                val rawMessage = message.substringAfter("::")
                val key = KeyManager.getPrivateKey()
                val decryptedMessage = encryptionStrategy.decrypt(rawMessage, key)
                ui.appendMessage("${originator.hostName}> $decryptedMessage")
            }

            MessageType.KEY_EXCHANGE_REQUEST -> {
                KeyManager.storeKeys(originator, message.substringAfter("::"), encryptionStrategy)

                val myKey = KeyManager.getPublicKey()
                val myKeyBytes = KeyGenerator.publicKeyToString(myKey)

                val response = MessageType.KEY_EXCHANGE_RESPONSE.getMessage(myKeyBytes)
                socket.sendMessage(originator, response)
            }

            MessageType.KEY_EXCHANGE_RESPONSE -> {
                KeyManager.storeKeys(originator, message.substringAfter("::"), encryptionStrategy)

                val messages = MessageBuffer.consume(originator) ?: return
                messages.forEach {
                    println(it)
                    val encryptionKey = KeyManager.getPublicKey(originator)!!
                    val encryptedMessage = encryptionStrategy.encrypt(it, encryptionKey)

                    val response = MessageType.PLAIN.getMessage(encryptedMessage)
                    socket.sendMessage(originator, response)
                }
            }

        }
    }

    init {
        socket.addListener(this)
    }
}