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
            .groupData(3)
            .map {it.reduce { s, x -> s + x  }}
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

infix fun<T> List<T>.groupData(n: Int): List<List<T>> {
    return (0 .. this.size-n)
        .map {this.slice(it until  it+n)}
}

fun main(args: Array<String>) {
    val challenge = Day1()
    challenge.main()
}