package aoc2021

import com.google.gson.Gson 

class Day8 {
    
    var input: String

    val validCombinations = mapOf(
        0 to "abcefg",
        1 to "cf",
        2 to "acdeg",
        3 to "acdfg",
        4 to "bcdf",
        5 to "abdfg",
        6 to "abdefg",
        7 to "acf",
        8 to "abcdefg",
        9 to "abcdfg"
    )
    
    init {
        val filename = "/aoc2021/day8/input.txt"
        input = Day8::class.java.getResource(filename).readText()
    }

    fun parseLine(s:String): Pair<List<String>, List<String>> =
        s.split("|")
            .map { p -> p.trim().split(" ").map { it.trim() }}
            .zipWithNext()
            .first()


    fun parseInput(s:String): List<Pair<List<String>, List<String>>> =
        s.split("\n")
            .filter {it.isNotEmpty()}
            .map { parseLine(it)}


    fun uniqueOutputPatterns(data: List<Pair<List<String>,  List<String>>>) =
        data
            .flatMap { it.second }
            .count { setOf(2, 3, 4, 7).contains(it.length)}

    fun allCombinations() : Map<Int, Set<Char>> =
        (0..9)
            .map { it to "abcdefg".toCharArray().toSet()}
            .toMap()


    fun part1(): String {
        val data = parseInput(input)
        return uniqueOutputPatterns(data).toString()
    }
   
    fun part2(): String? {
        return null
    }
    
}

fun main(args: Array<String>) {
    val challenge = Day8()
    val solutions = mapOf (
        "part1" to challenge.part1(),
        "part2" to challenge.part2(),
    )
    println("Solutions: ")
    println(Gson().toJson(solutions))
}