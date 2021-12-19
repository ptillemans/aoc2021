package aoc2021

import com.google.gson.Gson
import java.awt.SystemColor.text
import java.lang.System.lineSeparator

data class RegularNumber(var x: Long, val level: Int)
data class SnailFishNumber(val numbers: List<RegularNumber>)

fun String.toSnailFishNumber(): SnailFishNumber {
    var pos = 0
    var level = 0
    val numbers = mutableListOf<RegularNumber>()


    while (pos < this.length) {
        var c = this[pos]
        when {
            c == '[' -> {
                level += 1
            }
            c == ']' -> {
                level -= 1
            }
            c.isDigit() -> {
                var number: Long = 0
                while (this.get(pos).isDigit() && (pos < this.length)) {
                    number = 10 * number + this.get(pos).toString().toInt()
                    pos += 1
                }
                numbers.add(RegularNumber(number, level))
                pos -= 1
            }
        }
        pos += 1
    }
    return SnailFishNumber(numbers)
}

operator fun SnailFishNumber.plus(b: SnailFishNumber): SnailFishNumber =
    SnailFishNumber(this.numbers.map { r ->
        RegularNumber(r.x, r.level + 1)
    } + b.numbers.map { r ->
        RegularNumber(r.x, r.level + 1)
    }).reduce()

fun List<SnailFishNumber>.sum(): SnailFishNumber =
    this.reduce { sum, x -> sum + x}

fun SnailFishNumber.magnitude(): Long {
    val numbers = this.numbers.toMutableList()
    val maxLevel = numbers.map {it.level}.maxOrNull()?:0

    (1..maxLevel).reversed().forEach { level ->
        while (true) {
            val pos = numbers.indexOfFirst { it.level == level }
            if (pos < 0) {
                break
            }
            val mag = numbers[pos].x * 3 + numbers[pos+1].x*2
            numbers.removeAt(pos)
            numbers[pos] = RegularNumber(mag, level-1)
        }
    }
    return numbers.map { it.x }.sum()
}

fun List<SnailFishNumber>.homework(): Long =
    this.sum().magnitude()

fun SnailFishNumber.reduce(): SnailFishNumber =
    generateSequence(this) { it.reduceOnce() }.last()

fun SnailFishNumber.reduceOnce(): SnailFishNumber? {
    var reducedNumber = this.numbers.toMutableList()
    var pos: Int = reducedNumber.indexOfFirst { it.level > 4 }
    if (pos >= 0) {
        val left = reducedNumber.removeAt(pos).x
        val right = reducedNumber.removeAt(pos).x
        reducedNumber.add(pos, RegularNumber(0, 4))
        if (pos > 0) {
            reducedNumber[pos - 1].x += left
        }
        if (pos + 1 < reducedNumber.size) {
            reducedNumber[pos + 1].x += right
        }
        return SnailFishNumber(reducedNumber)
    }
    pos = reducedNumber.indexOfFirst { it.x >= 10 }
    if (pos >= 0) {
        val r = reducedNumber[pos]
        reducedNumber[pos] = RegularNumber(r.x / 2, r.level + 1)
        reducedNumber.add(pos + 1, RegularNumber(r.x - r.x / 2, r.level + 1))
        return SnailFishNumber(reducedNumber)
    }
    return null
}

fun List<SnailFishNumber>.highestMagnitudeSum(): Long =
    (0 until this.size).flatMap { a ->
        (0 until this.size).map { b ->
            Pair(a, b)
        }
    }
        .filter { it.first != it.second }
        .map { this[it.first] + this[it.second] }
        .maxOfOrNull { it.magnitude() } ?:-1


class Day18 {

    var input: String

    init {
        val filename = "/aoc2021/day18/input.txt"
        input = Day18::class.java.getResource(filename).readText()
    }


    fun part1(): String =
        input.split(lineSeparator())
            .filter { it.isNotBlank() }
            .map { it.toSnailFishNumber() }
            .sum()
            .magnitude()
            .toString()

    fun part2(): String =
        input.split(lineSeparator())
            .filter { it.isNotBlank() }
            .map { it.toSnailFishNumber() }
            .highestMagnitudeSum()
            .toString()

}

fun main() {
    val challenge = Day18()
    val solutions = mapOf(
        "part1" to challenge.part1(),
        "part2" to challenge.part2(),
    )
    println("Solutions: ")
    println(Gson().toJson(solutions))
}