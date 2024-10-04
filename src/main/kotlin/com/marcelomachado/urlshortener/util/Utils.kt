package com.marcelomachado.urlshortener.util

import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object Utils {

    fun md5(input: String): String {
        try {
            val md = MessageDigest.getInstance("MD5")
            val messageDigest = md.digest(input.toByteArray(StandardCharsets.UTF_8))
            val no = BigInteger(1, messageDigest)
            var hashText = no.toString(16)
            while (hashText.length < 32) {
                hashText = "0$hashText"
            }
            return hashText
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        }
    }
}