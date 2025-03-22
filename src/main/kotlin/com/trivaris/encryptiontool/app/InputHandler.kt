package com.trivaris.encryptiontool.app

import com.trivaris.encryptiontool.encryption.EncryptionStrategy
import com.trivaris.encryptiontool.keymanagement.KeyGenerator
import com.trivaris.encryptiontool.keymanagement.KeyManager
import com.trivaris.encryptiontool.networking.SecureSocket
import com.trivaris.encryptiontool.networking.messagehandlers.MessageBuffer
import com.trivaris.encryptiontool.networking.messagehandlers.MessageType
import java.net.InetAddress

class InputHandler(
    private val socket: SecureSocket,
    private val ui: UserInterface,
    private val strategy: EncryptionStrategy
) : InputListener {

    override fun onSendButtonClicked(message: String, recipient: InetAddress) {
        val encryptionKey = KeyManager.getPublicKey(recipient)
        val encryptedMessage =
            if (encryptionKey != null)
                MessageType.PLAIN.getMessage(strategy.encrypt(message, encryptionKey))
            else {
                ui.appendMessage("INFO> No public key, requesting...")
                MessageBuffer.store(message, recipient)
                val publicKey = KeyManager.getPublicKey()
                val publicKeyString = KeyGenerator.publicKeyToString(publicKey)
                MessageType.KEY_EXCHANGE_REQUEST.getMessage(publicKeyString)
            }

        socket.sendMessage(recipient, encryptedMessage)
    }

    init {
        ui.addListener(this)
    }
}