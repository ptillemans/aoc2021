package aoc2021

class Day1 {
    
    var input: String
    
    init {
        val filename = "/aoc2021/day1/input.txt"
        input = Day1::class.java.getResource(filename).readText()
    } 

    fun part1(): String =
        parseInput(input)
            .countIncreases()
            .toString()

    fun parseInput(text: String): List<Int> {
        return text.split("\n")
            .filter { it.isNotEmpty() }
            .map { it.toInt() }
    }

    fun part2(): String =
        parseInput(input)
            .windowed(size = 3, step = 1)
            .map {it.sum()}
            .countIncreases()
            .toString()
    
    fun main() {
        println("Part1: ")
        println(part1())
        println("Part2: ")
        println(part2())
    }

}

fun List<Int>.countIncreases(): Int {
    return this.zipWithNext()
        .map { it.first < it.second }
        .filter { it }
        .count()
}

fun main(args: Array<String>) {
    val challenge = Day1()
    challenge.main()
}