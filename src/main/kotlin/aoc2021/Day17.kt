package aoc2021

import com.google.gson.Gson
import common.Point
import common.minus
import java.lang.System.`in`
import java.lang.System.lineSeparator
import javax.swing.text.html.HTML.Tag.P
import kotlin.math.sqrt


class Day17 {
    
    var input: String
    
    init {
        val filename = "/aoc2021/day17/input.txt"
        input = Day17::class.java.getResource(filename).readText()
    }
    

    fun part1(): String? =
        input.parseTargetArea().maxHeight().toString()

    fun part2(): String? =
        input.parseTargetArea().goodSpeeds().count().toString()
    
}

fun String.parseTargetArea(): Pair<Point, Point> {
    val parts = this
        .removePrefix("target area: ")
        .removeSuffix(lineSeparator().toString())
        .split(", ")
        .flatMap { it.split("=")[1].split("..")}
        .map { it.trim() }
        .map { it.toInt() }
    return Pair(Point(parts[0], parts[2]), Point(parts[1], parts[3]))
}

fun Pair<Point, Point>.maxHeight(): Int {
    val yMaxDepth = this.first.second
    val Vy0 = -yMaxDepth - 1
    return Vy0*(Vy0 + 1)/2
}

fun Pair<Point, Point>.goodSpeeds(): List<Pair<Int, Int>>{
    val xMax = this.second.first
    val yMin = this.first.second

    val vXmax = xMax
    val vYmax = -yMin - 1
    val vXmin = (-1 + sqrt(1 - 8*this.first.first.toDouble())).toInt()
    val vYmin = yMin

    val goodSpeeds = mutableSetOf<Pair<Int, Int>>()

    return (vXmin..vXmax).flatMap { vX ->
        (vYmin .. vYmax).map { vY ->
            Pair(vX, vY)
        }
    }
        .filter{ this.hitsTarget(it) }
        .sorted()

}

fun  List<Pair<Int, Int>>.sorted(): List<Pair<Int, Int>> =
    this.sortedBy { p -> p.first*1000 + p.second }


fun Pair<Point, Point>.hitsTarget(initialSpeed: Pair<Int, Int>): Boolean {
    val xMax = this.second.first
    val yMin = this.first.second
    val xMin = this.first.first
    val yMax = this.second.second

    var speed = initialSpeed
    var pos = Point(0, 0)

    while (pos.first <= xMax && pos.second >= yMin) {
        pos = pos + speed
        if (speed.first > 0) {
            speed -= Pair(1, 1)
        } else {
            speed -= Pair(0, 1)
        }
        if (pos.first in xMin..xMax && pos.second in yMin..yMax) {
            return true
        }

    }
    return false
}

fun main() {
    val challenge = Day17()
    val solutions = mapOf (
        "part1" to challenge.part1(),
        "part2" to challenge.part2(),
    )
    println("Solutions: ")
    println(Gson().toJson(solutions))
}