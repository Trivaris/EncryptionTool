package com.trivaris.encryptiontool.encryption

import java.security.PrivateKey
import java.security.PublicKey
import java.util.Base64
import javax.crypto.Cipher

class RSAEncryption() : EncryptionStrategy {
    override val algorithm = "RSA"

    override fun encrypt(data: String, publicKey: PublicKey): String {
        val cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        val encrypted = cipher.doFinal(data.encodeToByteArray())
        return Base64.getEncoder().encodeToString(encrypted)
    }

    override fun decrypt(data: String, privateKey: PrivateKey): String {
        val cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        val decoded = Base64.getDecoder().decode(data)
        return cipher.doFinal(decoded).decodeToString()
    }
}