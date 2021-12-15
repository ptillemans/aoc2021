package aoc2021

import com.google.gson.Gson
import common.Point
import common.toPoint

typealias Line = Pair<Point, Point>
typealias CoverMap = Map<Point, Int>

class Day5 {
    
    var input: String
    
    init {
        val filename = "/aoc2021/day5/input.txt"
        input = Day5::class.java.getResource(filename).readText()
    }
    

    fun part1(): String {
        val lines = parseInput(input)
        val coverMap: CoverMap = lines.fold(mapOf()) { acc, line -> acc.addLine(line)}
        return coverMap.values
            .filter { it >= 2 }
            .count()
            .toString()
    }
   
    fun part2(): String {
        val lines = parseInput(input)
        val coverMap: CoverMap = lines.fold(mapOf()) { acc, line -> acc.addLine(line, true)}
        return coverMap.values
            .filter { it >= 2 }
            .count()
            .toString()
    }

    fun parseInput(input: String): List<Line> {
        return input.split("\n")
            .filter { it.isNotEmpty() }
            .map{ it.toLine() }
    }

}

fun List<Point>.toLine(): Line {
    require(this.size==2)
    val (p1, p2) = this
    return Line(p1, p2)
}

fun String.toLine(): Line =
    this.split(" -> ")
        .map { it.toPoint() }
        .toLine()


fun Line.coveredPoints(addDiagonals: Boolean = false): Set<Point> {
    fun sign(a:Int, b:Int): Int = if (a < b) 1 else if (a > b) -1 else 0
    fun span(a:Int, b:Int): IntProgression = IntProgression.fromClosedRange(a, b, sign(a, b))
    val (p1, p2) = this
    return when {
        (p1.first == p2.first) -> span(p1.second,p2.second).map { Point(p1.first, it) }
        (p1.second == p2.second) -> span(p1.first,p2.first).map { Point(it, p1.second) }
        else ->
            if (addDiagonals)
                span(p1.first, p2.first).zip(span(p1.second, p2.second))
            else
                listOf()

    }.toSet()
}

fun CoverMap.addLine(line: Line, addDiagonals: Boolean = false): CoverMap {
    val newCoverMap = this.toMutableMap()
    for (p in line.coveredPoints(addDiagonals)) {
        newCoverMap[p] = 1 + (newCoverMap[p]?:0);
    }
    return newCoverMap.toMap()
}





fun main() {
    val challenge = Day5()
    val solutions = mapOf (
        "part1" to challenge.part1(),
        "part2" to challenge.part2(),
    )
    println("Solutions: ")
    println(Gson().toJson(solutions))
}