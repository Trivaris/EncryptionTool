package com.trivaris.encryptiontool.encryption

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.math.BigInteger
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.RSAPrivateKeySpec

class RSAFactorizationAttack : CrackingStrategy {
    private val rsaStrategy = RSAEncryption()

    override fun crack(data: String, publicKey: PublicKey): String {

        val (n, e) = getModulusAndExponent(publicKey)
        val (p, q) = runBlocking { factorize(n) ?: throw IllegalArgumentException("Factorization failed") }

        val phiN = eulerTotient(p, q)
        val d = e.modInverse(phiN)

        val privateKey = createPrivateKey(n, d)
        val decoded = rsaStrategy.decrypt(data, privateKey)
        return decoded
    }

    suspend fun factorize(n: BigInteger): Pair<BigInteger, BigInteger>? {
        val sqrtN = n.sqrt()
        val chunkSize = 10000.toBigInteger()
        val deferredResults = mutableListOf<Deferred<Pair<BigInteger, BigInteger>?>>()

        val chunks = sqrtN.divide(chunkSize)

        for (i in 2..chunks.toInt()) {
            val start = i.toBigInteger() * chunkSize
            val end = i.toBigInteger().plus(BigInteger.ONE).times(chunkSize).minus(BigInteger.ONE).min(sqrtN)

            deferredResults.add(CoroutineScope(Dispatchers.IO).async {
                var i = start
                do {
                    println(i)
                    if (n.mod(i) == BigInteger.ZERO)
                        return@async Pair(i, n.divide(i))
                    i = i.add(BigInteger.ONE)
                } while (i <= end)

                return@async null
            })
        }

        deferredResults.forEach {
            val result = it.await()
            if (result != null) {
                println("Found: $result")
                return result
            }
        }
        println("Done, none found")
        return null
    }

    fun eulerTotient(p: BigInteger, q: BigInteger): BigInteger =
        (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE))

    fun getModulusAndExponent(publicKey: PublicKey): Pair<BigInteger, BigInteger> {
        if (publicKey !is RSAPublicKey)
            throw IllegalArgumentException("Public key is not an instance of RSAPublicKey")

        val modulus = publicKey.modulus
        val exponent = publicKey.publicExponent

        return Pair(modulus, exponent)
    }

    fun createPrivateKey(n: BigInteger, d: BigInteger): PrivateKey {
        val keySpec = RSAPrivateKeySpec(n, d)
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePrivate(keySpec)
    }

}