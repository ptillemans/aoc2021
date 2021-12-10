package aoc2021

import com.google.gson.Gson
import java.math.BigInteger

sealed class NavParseResult {
    object Valid : NavParseResult()
    data class Incomplete(val missingPart: String) : NavParseResult()
    data class Corrupt(val firstWrongChar: Char) : NavParseResult()
}

private val openingChar = mapOf(')' to '(', '}' to '{', ']' to '[', '>' to '<')
private val closingChar = mapOf('(' to ')', '[' to ']', '{' to  '}', '<' to '>')
private val charScore = mapOf(
    ')' to 3,
    ']' to 57,
    '}' to 1197,
    '>' to 25137,
)


class Day10 {

    var input: List<String>

    init {
        val filename = "/aoc2021/day10/input.txt"
        input = Day10::class.java.getResource(filename).readText().split("\n").filter { it.isNotEmpty() }
    }


    fun part1(): String =
        input
            .map { it.checkSyntax() }
            .filterIsInstanceTo(mutableListOf<NavParseResult.Corrupt>())
            .map { it -> charScore[it.firstWrongChar]}
            .filterNotNull()
            .sum()
            .toString()

    fun part2(): String?  {
        val scores = input
            .map { it.checkSyntax() }
            .filterIsInstanceTo(mutableListOf<NavParseResult.Incomplete>())
            .map { it -> it.missingPart.scoreMissing()}
            .sorted()

        return scores[scores.size/2].toString()
    }

}

fun String.checkSyntax(): NavParseResult {
    val stack = ArrayDeque<Char>()
    val data = ArrayDeque<Char>(this.toCharArray().toList())

    var result: NavParseResult = NavParseResult.Valid

    while (result == NavParseResult.Valid && data.isNotEmpty()) {
        val char = data.removeFirst()
        result = checkChar(char, stack)
    }

    if (result == NavParseResult.Valid && stack.isNotEmpty()) {
        result = NavParseResult.Incomplete(stack.map {  closingChar[it]!! }.joinToString(""))
    }
    return result
}

private fun checkChar(c: Char, stack: ArrayDeque<Char>): NavParseResult {
    val openingChar: Char? =  openingChar[c]

    if (openingChar == null) {
        stack.addFirst(c)
    } else if (stack.isNotEmpty() && openingChar == stack.first()) {
        stack.removeFirst()
    } else {
        return NavParseResult.Corrupt(c)
    }

    return NavParseResult.Valid
}

fun String.scoreMissing(): BigInteger =
    this.toCharArray()
        .map {
            when(it) {
                ')' -> BigInteger.valueOf(1)
                ']' -> BigInteger.valueOf(2)
                '}' -> BigInteger.valueOf(3)
                '>' -> BigInteger.valueOf(4)
                else -> BigInteger.valueOf(0)
            }
        }
        .reduce {acc, x -> BigInteger.valueOf(5)*acc + x }


fun main() {
    val challenge = Day10()
    val solutions = mapOf(
        "part1" to challenge.part1(),
        "part2" to challenge.part2(),
    )
    println("Solutions: ")
    println(Gson().toJson(solutions))
}