package aoc2021

import com.google.gson.Gson

data class HeightMap(val nRows: Int, val body: List<Int>, val default: Int = Int.MAX_VALUE) {
    val nCols: Int = body.size / nRows

    fun element(x: Int, y: Int): Int =
        if (x in 0 until nCols && y in 0 until nRows)
            body[y * nCols + x]
        else
            default

    fun element(pos: Pair<Int, Int>): Int = pos.let{ (x, y) -> element(x, y) }

    fun row(r: Int): Any? = body.slice(r * nCols until (r + 1) * nCols)

    fun column(c: Int): Any? = (c until c + nRows * nCols step nCols).map { body[it] }

}


class Day9 {

    var input: String

    init {
        val filename = "/aoc2021/day9/input.txt"
        input = Day9::class.java.getResource(filename).readText()
    }


    fun part1(): String =
        input.toHeightMap()
            .riskLevel()
            .toString()

    fun part2(): String =
        input.toHeightMap()
            .findBasinsDecreasingSizes()
            .take(3)
            .reduce { acc, x -> acc * x }
            .toString()

}

fun String.toHeightMap(): HeightMap {
    val lines = this.trim().split("\n")
    val nRows = lines.size
    val body = this.trim()
        .split("")
        .filter { it.isNotBlank() }
        .map { it.toInt() }
    return HeightMap(nRows, body)
}

val neighbours = listOf(
    Pair(-1, 0),
    Pair(1, 0),
    Pair(0, -1),
    Pair(0, 1)
)

fun HeightMap.isLocalMinima(p: Pair<Int, Int>): Boolean {
    val result =  this.element(p).let { v ->
        neighbours.map { dp ->
                this.element(p + dp).let {
                        vNeighbour -> v < vNeighbour
                }
        }
    }
    return result.all{it}
}

operator fun Pair<Int, Int>.plus(b : Pair<Int, Int>) = Pair(this.first+b.first, this.second+b.second)

fun HeightMap.localMinima(): Set<Pair<Int, Int>> =
    (0 until this.nRows).flatMap { y ->
        (0 until this.nCols).map { x ->
            Pair(x, y)
        }
    }
        .filter { this.isLocalMinima(it) }
        .toSet()

fun HeightMap.riskLevel(): Int =
    this.localMinima()
        .map { this.element(it) + 1}
        .sum()

fun HeightMap.findBasin(minima: Pair<Int, Int>): Set<Pair<Int, Int>> {
    var basin: MutableSet<Pair<Int, Int>> = mutableSetOf(minima)
    val open = ArrayDeque<Pair<Int,Int>>()
    open.add(minima)

    while (open.isNotEmpty()) {
        val location = open.removeFirst()
        val next = neighbours
            .map { location + it}
            .filter { !basin.contains(it)}
            .filter { this.element(it) < 9 }
        open.addAll(next)
        basin.addAll(next)
    }
    return basin
}

fun HeightMap.findBasinsDecreasingSizes(): List<Int> =
    this.localMinima()
        .map { this.findBasin(it).size }
        .sortedBy { -it }


fun main() {
    val challenge = Day9()
    val solutions = mapOf(
        "part1" to challenge.part1(),
        "part2" to challenge.part2(),
    )
    println("Solutions: ")
    println(Gson().toJson(solutions))
}