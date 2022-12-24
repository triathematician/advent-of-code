package aoc

import java.math.BigInteger
import java.security.MessageDigest

class AocDay4: AocDay(4) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay4().run() } }

    override val testinput = "abcdef"
    override val input = "yzbqklnj"

    val md5 = MessageDigest.getInstance("MD5")

    fun md5(input: String) = md5.let {
        BigInteger(1, md5.digest(input.toByteArray()))
            .toString(16).padStart(32, '0')
    }

    override fun calc1(input: String) = (1..10000000).first {
        md5(input + it).startsWith("00000")
    }
    override fun calc2(input: String) = (1..10000000).first {
        md5(input + it).startsWith("000000")
    }
}