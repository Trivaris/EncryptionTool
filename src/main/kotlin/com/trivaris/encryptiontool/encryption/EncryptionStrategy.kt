package com.trivaris.encryptiontool.encryption

import java.security.PrivateKey
import java.security.PublicKey

interface EncryptionStrategy {
    val algorithm: String

    fun encrypt(data: String, publicKey: PublicKey): String
    fun decrypt(data: String, privateKey: PrivateKey): String
}