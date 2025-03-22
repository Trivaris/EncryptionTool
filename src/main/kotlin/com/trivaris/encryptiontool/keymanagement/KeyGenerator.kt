package com.trivaris.encryptiontool.keymanagement

import com.trivaris.encryptiontool.encryption.EncryptionStrategy
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

object KeyGenerator {
    fun saveKeypair(strategy: EncryptionStrategy) {
        val keyPair = generateKeyPair(strategy)
        KeyManager.setKeyPair(keyPair)
    }

    private fun generateKeyPair(strategy: EncryptionStrategy): KeyPair {
        val keyGen = KeyPairGenerator.getInstance(strategy.algorithm)
        keyGen.initialize(2048)
        return keyGen.genKeyPair()
    }

    fun publicFromString(publicKey: String, strategy: EncryptionStrategy): PublicKey {
        val decodedKey = Base64.getDecoder().decode(publicKey)
        val keySpec = X509EncodedKeySpec(decodedKey)
        val keyFactory = KeyFactory.getInstance(strategy.algorithm)
        return keyFactory.generatePublic(keySpec)
    }

    fun publicKeyToString(publicKey: PublicKey): String {
        return Base64.getEncoder().encodeToString(publicKey.encoded)
    }
}