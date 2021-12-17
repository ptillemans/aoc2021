package aoc2021

import com.google.gson.Gson

sealed class Packet(open val version: Int) {
    abstract fun eval(): Long
}

data class LiteralPacket(override val version: Int, val payload: Long) : Packet(version) {
    override fun eval(): Long = payload


}

open class OperatorPacket(override val version: Int, open val payload: List<Packet>) : Packet(version) {
    override fun eval(): Long {
        TODO("Not yet implemented")
    }
}

data class SumPacket(override val version: Int, override val payload: List<Packet>) : OperatorPacket(version, payload) {
    override fun eval(): Long =
        payload.map { it.eval() }.sum()
}

data class ProductPacket(override val version: Int, override val payload: List<Packet>) : OperatorPacket(version, payload) {
    override fun eval(): Long =
        payload.map { it.eval() }.reduce(Long::times)
}

data class MinimumPacket(override val version: Int, override val payload: List<Packet>) : OperatorPacket(version, payload) {
    override fun eval(): Long =
        payload.minOf { it.eval() }
}

data class MaximumPacket(override val version: Int, override val payload: List<Packet>) : OperatorPacket(version, payload) {
    override fun eval(): Long =
        payload.maxOf { it.eval() }
}

data class GreaterThanPacket(override val version: Int, override val payload: List<Packet>) : OperatorPacket(version, payload) {
    override fun eval(): Long =
        if (payload[0].eval() > payload[1].eval()) 1 else 0
}

data class LessThanPacket(override val version: Int, override val payload: List<Packet>) : OperatorPacket(version, payload) {
    override fun eval(): Long =
        if (payload[0].eval() < payload[1].eval()) 1 else 0
}

data class EqualToPacket(override val version: Int, override val payload: List<Packet>) : OperatorPacket(version, payload) {
    override fun eval(): Long =
        if (payload[0].eval() == payload[1].eval()) 1 else 0
}

enum class PacketType() {
    SUM,
    PRODUCT,
    MINIMUM,
    MAXIMUM,
    LITERAL,
    GREATER,
    LESS,
    EQUAL,
}

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
        .filterNotNull()
        .joinToString("")

fun String.parsePacket(): Packet =
    this.hexToBin().binaryParsePacket().first


fun String.binaryParsePacket(): Pair<Packet, String> {
    val version = this.slice(0 until 3).binToInt()
    val typeId = PacketType.values()[this.slice(3 until 6).binToInt()]
    val binary = this.slice(6 until this.length)
    return when (typeId) {
        PacketType.SUM -> binary.parseOperatorPacket(version).toSum()
        PacketType.PRODUCT -> binary.parseOperatorPacket(version).toProduct()
        PacketType.MINIMUM -> binary.parseOperatorPacket(version).toMinimum()
        PacketType.MAXIMUM -> binary.parseOperatorPacket(version).toMaximum()
        PacketType.LITERAL -> binary.parseLiteralPacket(version)
        PacketType.GREATER -> binary.parseOperatorPacket(version).toGreaterThan()
        PacketType.LESS -> binary.parseOperatorPacket(version).toLessThan()
        PacketType.EQUAL -> binary.parseOperatorPacket(version).toEqualTo()
    }
}

private fun Pair<OperatorPacket, String>.toSum() =
    Pair(SumPacket(this.first.version, this.first.payload), this.second)
private fun Pair<OperatorPacket, String>.toProduct() =
    Pair(ProductPacket(this.first.version, this.first.payload), this.second)
private fun Pair<OperatorPacket, String>.toMinimum() =
    Pair(MinimumPacket(this.first.version, this.first.payload), this.second)
private fun Pair<OperatorPacket, String>.toMaximum() =
    Pair(MaximumPacket(this.first.version, this.first.payload), this.second)
private fun Pair<OperatorPacket, String>.toGreaterThan() =
    Pair(GreaterThanPacket(this.first.version, this.first.payload), this.second)
private fun Pair<OperatorPacket, String>.toLessThan() =
    Pair(LessThanPacket(this.first.version, this.first.payload), this.second)
private fun Pair<OperatorPacket, String>.toEqualTo() =
    Pair(EqualToPacket(this.first.version, this.first.payload), this.second)

private fun String.parseOperatorPacket(version: Int): Pair<OperatorPacket, String> {
    var s : String
    val packets = mutableListOf<Packet>()
    val lType = this.slice(0..0)
    if (lType == "0") {
        val len = this.slice(1 .. 15).binToInt()
        s = this.slice(16 until 16 + len)
        while (s.length > 4) {
            val p = s.binaryParsePacket()
            s = p.second
            packets.add(p.first)
        }
        s = this.substring(16+len)
    } else {
        val len = this.slice(1 .. 11).binToInt()
        s = this.substring(12)
        (0 until len).forEach {
            val p = s.binaryParsePacket()
            s = p.second
            packets.add(p.first)
        }
    }
    return Pair(
        OperatorPacket(version, packets),
        s
    )
}

private fun String.parseLiteralPacket(
    version: Int,
): Pair<LiteralPacket, String> {
    var s = this
    var payload: Long = 0
    while (true) {
        val contBit = s.slice(0 until 1)
        val digit = s.slice( 1 .. 4).binToInt()
        s = s.substring(5)
        payload = 16 * payload + digit
        if (contBit == "0") {
            break
        }
    }
    val packet = LiteralPacket(version, payload)
    return Pair(packet, s)
}

private fun String.binToInt(): Int =
    this.split("")
        .filter { it.isNotEmpty() }
        .map { it.toInt() }
        .reduce { acc, x -> 2 * acc + x }

fun Packet.sumOfVersions(): Int =
    when(this) {
        is LiteralPacket -> { this.version }
        is OperatorPacket -> { this.version + this.payload.map {it.sumOfVersions()}.sum() }
    }


class Day16 {

    var input: String

    init {
        val filename = "/aoc2021/day16/input.txt"
        input = Day16::class.java.getResource(filename)!!.readText()
    }


    fun part1(): String =
        input.parsePacket().sumOfVersions().toString()


    fun part2(): String =
        input.parsePacket().eval().toString()

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