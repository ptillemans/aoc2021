package aoc2021

import com.google.gson.Gson
import java.io.File
import java.lang.System.lineSeparator

sealed class Fold{
    abstract fun doFold(points: Set<Point>): Set<Point>
    data class HorizontalFold(val x: Int): Fold() {
        override fun doFold(points: Set<Point>): Set<Point> =
            points
                .map { p -> if (p.first > x) Point(2*x-p.first, p.second) else p}
                .toSet()
    }

    data class VerticalFold(val y: Int): Fold() {
        override fun doFold(points: Set<Point>): Set<Point> =
            points
                .map { p -> if (p.second > y) Point(p.first, 2*y - p.second) else p}
                .toSet()

    }
}

data class Day13Input(val points: Set<Point>, val folds: List<Fold>)

class Day13 {
    
    var input: Day13Input
    
    init {
        val filename = "/aoc2021/day13/input.txt"
        input = Day13::class.java.getResource(filename)!!.readText().parseInputDay13()
    }

    fun part1(): String =
        input.folds[0].doFold(input.points).size.toString()

   
    fun part2(): String =
        input.applyFolds().prettyPrint().format().writeTo("day13_part2.txt")

    
}

fun String.parseInputDay13(): Day13Input {
    val parts = this.split(lineSeparator()+ lineSeparator())
    val points = parts[0].split(lineSeparator())
        .flatMap{it.split(',').map {it.toInt()}.zipWithNext()}
        .toSet()
    val folds = parts[1].split(lineSeparator())
        .map {it.removePrefix("fold along ").trim().split("=")}
        .filter { it.size == 2 }
        .map{ if (it[0]=="x")
            Fold.HorizontalFold(it[1].toInt()) as Fold
        else
            Fold.VerticalFold(it[1].toInt()) as Fold
        }
    return Day13Input(points, folds)
}

fun Day13Input.applyFolds(): Set<Point> =
    this.folds
        .fold(this.points) {points, fold -> fold.doFold(points)}

fun Set<Point>.format(): String =
    this.map { it.first.toString() + " " + it.second.toString() }
        .joinToString(lineSeparator())

fun Set<Point>.prettyPrint(): Set<Point> {
    val maxX = this.maxOf { it.first }
    val maxY = this.maxOf { it.second }
    var output = (0 .. maxY).map { " ".repeat(maxX + 1)}.toMutableList()
    this.forEach { (x, y) -> output[y] =
        output[y].slice(0 until x) +
                "*" +
                output[y].slice(x+1 .. maxX)}
    println(output.joinToString ( lineSeparator()))
    return this
}

fun String.writeTo(filename: String): String {
    File(filename).writeText(this)
    return this
}

fun main() {
    val challenge = Day13()
    val solutions = mapOf (
        "part1" to challenge.part1(),
        "part2" to challenge.part2(),
    )
    println("Solutions: ")
    println(Gson().toJson(solutions))
}