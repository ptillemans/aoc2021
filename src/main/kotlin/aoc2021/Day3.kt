package aoc2021

import com.google.gson.Gson


class Day3 {

    var input: String

    init {
        val filename = "/aoc2021/day3/input.txt"
        input = Day3::class.java.getResource(filename)!!.readText()
    }

    fun parseInput(text: String): List<List<Int>> {
        return text.split("\n")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .map { it.toVector() }
    }


    fun part1(): String {
        val data = parseInput(input)
        val gamma = data.calculateGamma()
        val epsilon = data.calculateEpsilon()
        return (gamma * epsilon).toString()
    }

    fun part2(): String {
        val data = parseInput(input)
        val o2Rating = data.oxygenGeneratorRating()
        val co2Rating = data.co2ScrubberRating()
        return (o2Rating * co2Rating).toString()
    }

}

fun String.toVector(): List<Int> {
    val parts = this.split("").filter { it.isNotEmpty() }
    return parts.map {
        it.toInt()
    }
}

fun List<Int>.addVector(b: List<Int>): List<Int> =
    this
        .zip(b)
        .map { it.first + it.second }

fun List<List<Int>>.calculateGamma(): Int =
    this.calculateFactor { x: Int -> x >= size / 2 }

fun List<List<Int>>.calculateEpsilon(): Int =
    this.calculateFactor { x: Int -> x <= size / 2 }


private fun List<List<Int>>.calculateFactor(
    pred: (Int) -> Boolean
): Int =
   this
        .countBits()
        .map { if (pred(it)) 1 else 0 }
        .reduce { acc, x -> 2 * acc + x }


private fun List<List<Int>>.countBits() =
    this.reduce { a, b -> a.addVector(b) }

fun List<List<Int>>.oxygenGeneratorRating(): Int =
    this.calculateRating { x: Int, l: Int -> x * 2 >= l }


fun List<List<Int>>.co2ScrubberRating(): Int =
    this.calculateRating { x: Int, l: Int -> x * 2 < l}

fun List<List<Int>>.calculateRating(pred: (Int, Int) -> Boolean): Int {
    val len = this[0].size
    var data = this
    for (i in 0..Int.MAX_VALUE) {
        val bitNr = i % len
        data = data.scrubBits(bitNr) { x -> pred(x, data.size)}
        if (data.size <= 1) {
            break
        }
    }
    return data[0].reduce  { acc, x -> 2 * acc + x }
}

fun List<List<Int>>.scrubBits(pos: Int, pred: (Int) -> Boolean) : List<List<Int>> {
    val bitCount = this.countBits()[pos]
    val filterBit = if (pred(bitCount)) 1 else 0
    return this.filter { it[pos] == filterBit }
}


fun main() {
    val challenge = Day3()
    val solutions = mapOf(
        "part1" to challenge.part1(),
        "part2" to challenge.part2(),
    )
    println("Solutions: ")
    println(Gson().toJson(solutions))
}