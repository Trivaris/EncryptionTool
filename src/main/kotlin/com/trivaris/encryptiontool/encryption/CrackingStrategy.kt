package com.trivaris.encryptiontool.encryption

import java.security.PublicKey

interface CrackingStrategy {
    fun crack(data: String, publicKey: PublicKey): String
}