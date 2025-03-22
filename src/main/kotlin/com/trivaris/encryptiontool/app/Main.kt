package com.trivaris.encryptiontool.app

import com.trivaris.encryptiontool.encryption.RSAEncryption
import com.trivaris.encryptiontool.keymanagement.KeyGenerator
import com.trivaris.encryptiontool.networking.SecureSocket
import com.trivaris.encryptiontool.networking.messagehandlers.MessageHandler
import javax.swing.SwingUtilities


fun main() {
    val socket = SecureSocket(8080)
    val ui = UserInterface()
    val strategy = RSAEncryption()

    KeyGenerator.saveKeypair(strategy)
    MessageHandler(socket, ui, strategy)
    InputHandler(socket, ui, strategy)

    SwingUtilities.invokeLater {
        ui.show()
    }
}