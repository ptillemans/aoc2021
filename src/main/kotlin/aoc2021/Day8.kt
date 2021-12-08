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
            .map { p -> p.trim()
                .split(" ")
                .map { it.trim().toCharArray().sorted().joinToString("") }
            }
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
        val data = parseInput(input)
        return data.sumOf { lineOutput(it) }.toString()
    }

    fun mapDigits(digits: List<String>): Map<String, Int> {
        val digit1 = digits.filter { it.length == 2 }.first()

        // first 5 are easy, 4 have unique length
        // 2 is the only one missing segment f
        var mapping = mapOf<String, Int>(
            digit1 to 1,
            findDigit2(digits) to 2,
            digits.filter { it.length == 4 }.first() to 4,
            digits.filter { it.length == 3 }.first() to 7,
            digits.filter { it.length == 7 }.first() to 8,
        )

        // 0 and 3 are the only one remaining missing just segment b or d
        var remaining = digits.filter { !mapping.keys.contains(it) }
        mapping = findDigit03(remaining).fold(mapping) { acc, p -> acc + p}

        // 5 is the only one remaining with 5 segments
        remaining = remaining.filter { !mapping.keys.contains(it) }
        mapping = mapping + (remaining.first{it.length == 5} to 5)

        // 9 contains same segments as 1, 6 does not
        remaining = remaining.filter { !mapping.keys.contains(it) }
        val (digit9, digit6) = remaining.partition { s -> digit1.toCharArray().all { c -> s.contains(c) } }
        return mapping.plus(digit6.first() to 6).plus(digit9.first() to 9)
    }

    fun findDigit2(digits: List<String>): String {
        val freq = digits
            .fold(mapOf<Char, Int>()) { acc, x ->
                x.toCharArray().fold(acc) { acc, c -> acc + Pair(c, (acc[c]?:0) + 1)}
            }
        val segment = freq.filter { it.value == 9 }.map { it.key }.first()
        return digits.first { !it.contains(segment) }
    }

    fun findDigit03(digits: List<String>): List<Pair<String, Int>> {
        val freq = digits
            .fold(mapOf<Char, Int>()) { acc, x ->
                x.toCharArray().fold(acc) { acc, c -> acc + Pair(c, (acc[c]?:0) + 1)}
            }
        return freq
            .filter { it.value == 4 }
            .map { it.key }
            .flatMap { segment -> digits.filter { !it.contains(segment)} }
            .map { Pair(it, if (it.length==6) 0 else 3)}
    }

    fun lineOutput(line: Pair<List<String>, List<String>>): Int {
        val mapping = mapDigits(line.first)
        return line.second.map { mapping[it]?:0 }.reduce {acc, x -> 10*acc + x}
    }

}

fun main() {
    val challenge = Day8()
    val solutions = mapOf(
        "part1" to challenge.part1(),
        "part2" to challenge.part2(),
    )
    println("Solutions: ")
    println(Gson().toJson(solutions))
}