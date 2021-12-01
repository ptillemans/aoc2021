package aoc2021

class Day1 {
    
    var input: String
    
    init {
        val filename = "/aoc2021/day1/input.txt"
        input = Day1::class.java.getResource(filename).readText()
    } 

    fun part1(): String? {
        val data = parseInput(input)
        val answer = countIncreases(data)

        return answer.toString()
    }

    fun countIncreases(data: List<Int>): Int {
        return data.zipWithNext()
            .map { it.first < it.second }
            .filter { it }
            .count()
    }

    fun parseInput(input: String): List<Int> {
        return input.split("\n")
            .filter { it.isNotEmpty() }
            .map { it.toInt() }
    }

    fun part2(): String?{
        val data = parseInput(input)
        val groupSums = groupData(3, data)
            .map {it.reduce { s, x -> s + x  }}
        return countIncreases(groupSums).toString()
    }
    
    fun main() {
        println("Part1: ")
        println(part1())
        println("Part2: ")
        println(part2())
    }

    fun groupData(n: Int, data: List<Int>): List<List<Int>> {
        return (0 .. data.size-n)
            .map {data.slice(it until  it+n)}
    }
}

fun main(args: Array<String>) {
    val challenge = Day1()
    challenge.main()
}