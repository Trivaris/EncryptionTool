package com.trivaris.encryptiontool.keymanagement

import com.trivaris.encryptiontool.encryption.EncryptionStrategy
import java.net.InetAddress
import java.security.KeyPair
import java.security.PrivateKey
import java.security.PublicKey

object KeyManager {
    private val keyStore: MutableMap<InetAddress, PublicKey> = mutableMapOf()
    private lateinit var keypair: KeyPair

    fun storeKeys(originator: InetAddress, publicKey: String, strategy: EncryptionStrategy) {
        val theirKey = KeyGenerator.publicFromString(publicKey, strategy)
        storeKeys(originator, theirKey)
    }

    fun storeKeys(address: InetAddress, publicKey: PublicKey) {
        keyStore[address] = publicKey
    }

    fun getPublicKey(address: InetAddress): PublicKey? = keyStore[address]
    fun getPublicKey(): PublicKey = keypair.public
    fun getPrivateKey(): PrivateKey = keypair.private

    fun setKeyPair(keypair: KeyPair) {
        this.keypair = keypair
    }
}