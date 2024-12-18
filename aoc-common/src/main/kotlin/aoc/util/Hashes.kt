package aoc.util

import java.math.BigInteger
import java.security.MessageDigest

object Hashes {
    val md5 = MessageDigest.getInstance("MD5")

    fun String.md5() = md5.let {
        BigInteger(1, md5.digest(toByteArray()))
            .toString(16).padStart(32, '0')
    }
}