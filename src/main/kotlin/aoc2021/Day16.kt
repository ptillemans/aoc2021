package aoc2021

import com.google.gson.Gson

sealed class Packet(
    val version: Int,
)

class LiteralPacket(version: Int, val payload: List<Int>) : Packet(version)
class OperatorPacket(version: Int, val payload: List<Packet>) : Packet(version)


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
    var binary = this.splitToSequence("").filter { it.isNotEmpty() }
    val version = binary.take(3).toInt()
    binary = binary.drop(3)
    val typeId = binary.take(3).toInt()
    binary = binary.drop(3)
    when (typeId) {
        0 -> return binary.parseOperatorPacketType0(version)
        4 -> return binary.parseLiteralPacket(version)
        else -> return null
    }
}

private fun <T> Sequence<T>.parseOperatorPacketType0(version: Int): Pair<Packet, String>? {
    TODO("Not yet implemented")
}

private fun  Sequence<String>.parseLiteralPacket(
    version: Int,
): Pair<LiteralPacket, String> {
    var seq = this
    var digits = mutableListOf<Int>()
    while (true) {
        val contBit = seq.take(1)
        seq = seq.drop(1)
        val digit = seq.take(4).toInt()
        seq = seq.drop(4)
        digits.add(digit)
        if (contBit.first() == "0") {
            break
        }
    }
    val packet = LiteralPacket(version, digits)
    return Pair(packet, seq.joinToString(""))
}

private fun Sequence<String>.toInt(): Int =
    this.map { it.toInt() }
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