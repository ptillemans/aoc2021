package aoc2021

import com.google.gson.Gson

sealed class Packet(open val version: Int)

data class LiteralPacket(override val version: Int, val payload: List<Int>) : Packet(version)
data class Operator0Packet(override val version: Int, val payload: List<Packet>) : Packet(version)
data class Operator1Packet(override val version: Int, val payload: List<Packet>) : Packet(version)


val hexToDecMap = mapOf(
    "0" to "0000",
    "1" to "0001",
    "2" to "0010",
    "3" to "0011",
    "4" to "0100",
    "5" to "0101",
    "6" to "0110",
    "7" to "0111",
    "8" to "1000",
    "9" to "1001",
    "A" to "1010",
    "B" to "1011",
    "C" to "1100",
    "D" to "1101",
    "E" to "1110",
    "F" to "1111",
)

fun String.hexToBin(): String =
    this.split("")
        .map { hexToDecMap[it] }
        .filter { it != null }
        .joinToString("")


fun String.parsePacket(): Pair<Packet, String>? {
    val version = this.slice(0 until 3).binToInt()
    val typeId = this.slice(3 until 6).binToInt()
    val binary = this.slice(6 until this.length)
    when (typeId) {
        0 -> return binary.parseOperator0Packet(version)
        4 -> return binary.parseLiteralPacket(version)
        else -> return null
    }
}

private fun String.parseOperator0Packet(version: Int): Pair<Packet, String>? {
    TODO("Not yet implemented")
}

private fun String.parseLiteralPacket(
    version: Int,
): Pair<LiteralPacket, String> {
    var s = this
    var digits = mutableListOf<Int>()
    while (true) {
        val contBit = s.slice(0 until 1)
        val digit = s.slice( 1 .. 4).binToInt()
        s = s.substring(5)
        digits.add(digit)
        if (contBit == "0") {
            break
        }
    }
    val packet = LiteralPacket(version, digits)
    return Pair(packet, s)
}

private fun String.binToInt(): Int =
    this.split("")
        .filter { it.isNotEmpty() }
        .map { it.toInt() }
        .reduce { acc, x -> 2 * acc + x }

class Day16 {

    var input: String

    init {
        val filename = "/aoc2021/day16/input.txt"
        input = Day16::class.java.getResource(filename)!!.readText()
    }


    fun part1(): String? {
        return null
    }

    fun part2(): String? {
        return null
    }

}

fun main() {
    val challenge = Day16()
    val solutions = mapOf(
        "part1" to challenge.part1(),
        "part2" to challenge.part2(),
    )
    println("Solutions: ")
    println(Gson().toJson(solutions))
}