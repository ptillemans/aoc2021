package aoc2021

import com.google.gson.Gson
import common.Point

class Day17 {
    
    var input: String
    
    init {
        val filename = "/aoc2021/day17/input.txt"
        input = Day17::class.java.getResource(filename).readText()
    }
    

    fun part1(): String? {
        return null
    }
   
    fun part2(): String? {
        return null
    }
    
}

fun String.parseTargetArea(): Pair<Point, Point> {
    val parts = this
        .removePrefix("target area: ")
        .split(", ")
        .flatMap { it.split("=")[1].split("..")}
        .map { it.toInt() }
    return Pair(Point(parts[0], parts[2]), Point(parts[1], parts[3]))
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