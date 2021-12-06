package aoc2021

import com.google.gson.Gson
import common.toIntList
import java.math.BigInteger

typealias PopulationFrequency = Map<Int, BigInteger>

val ZERO = BigInteger.valueOf(0)

class Day6 {

    var input: List<Int>
    
    init {
        val filename = "/aoc2021/day6/input.txt"
        input = Day6::class.java.getResource(filename).readText().trim().toIntList()
    }
    

    fun part1(): String =
        (0 until 80)
            .fold(input.toPopulationFrequency()){ acc, _ -> acc.newGeneration()}
            .map { it.value }
            .reduce { acc, x -> acc + x}
            .toString()


    fun part2(): String =
        (0 until 256)
            .fold(input.toPopulationFrequency()){ acc, _ -> acc.newGeneration()}
            .map { it.value }
            .reduce { acc, x -> acc + x}
            .toString()

}

fun PopulationFrequency.newGeneration(): PopulationFrequency =
    this.map { (it.key - 1) to it.value}
        .toMap()
        .plus(Pair(6, (this[0]?: ZERO) + (this[7]?: ZERO))) //reset + previous born
        .plus(Pair(8, this[0]?: ZERO))                      // newly born
        .minus(-1)                                          // remove previous 0 entry
        .filter { (_, v) -> v != ZERO}                      // clean zero entries

fun List<Int>.toPopulationFrequency():PopulationFrequency =
    this.sorted()
        .groupBy { it }
        .map { (k, v) -> k to BigInteger.valueOf(v.size.toLong()) }
        .toMap()


fun main(args: Array<String>) {
    val challenge = Day6()
    val solutions = mapOf (
        "part1" to challenge.part1(),
        "part2" to challenge.part2(),
    )
    println("Solutions: ")
    println(Gson().toJson(solutions))
}