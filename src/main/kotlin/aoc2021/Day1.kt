package aoc2021
import com.google.gson.Gson

class Day1 {
    
    var input: String
    
    init {
        val filename = "/aoc2021/day1/input.txt"
        input = Day1::class.java.getResource(filename).readText()
    }

    fun parseInput(text: String): List<Int> {
        return text.split("\n")
            .filter { it.isNotEmpty() }
            .map { it.toInt() }
    }

    fun part1(): String =
        parseInput(input)
            .countIncreases()
            .toString()

    fun part2(): String =
        parseInput(input)
            .windowed(size = 3, step = 1)
            .map {it.sum()}
            .countIncreases()
            .toString()
    
}

fun List<Int>.countIncreases(): Int {
    return this.zipWithNext()
        .count { it.first < it.second }
}

fun main(args: Array<String>) {
    val challenge = Day1()
    val solutions = mapOf (
        "part1" to challenge.part1(),
        "part2" to challenge.part2(),
    )
    println("Solutions: ${Gson().toJson(solutions)}")
}