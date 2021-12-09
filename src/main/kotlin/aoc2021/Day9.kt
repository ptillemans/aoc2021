package aoc2021

import com.google.gson.Gson
import common.IntMatrix
import common.toIntMatrix

typealias HeightMap = IntMatrix

class Day9 {

    var input: String

    init {
        val filename = "/aoc2021/day9/input.txt"
        input = Day9::class.java.getResource(filename)!!.readText()
    }


    fun part1(): String =
        input.toIntMatrix(invalidValue=9)
            .riskLevel()
            .toString()

    fun part2(): String =
        input.toIntMatrix(9)
            .findBasinsDecreasingSizes()
            .take(3)
            .reduce { acc, x -> acc * x }
            .toString()

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
        (0 until this.nColumns).map { x ->
            Pair(x, y)
        }
    }
        .filter { this.isLocalMinima(it) }
        .toSet()

fun HeightMap.riskLevel(): Int =
    this.localMinima().sumOf { this.element(it) + 1 }

fun HeightMap.findBasin(minima: Pair<Int, Int>): Set<Pair<Int, Int>> {
    var basin: MutableSet<Pair<Int, Int>> = mutableSetOf()
    val open = ArrayDeque<Pair<Int,Int>>()

    // put the first seed of the basin
    open.add(minima)

    // let the basin grow around the seed
    while (open.isNotEmpty()) {
        // take next location from the unprocessed locations
        val location = open.removeFirst()
        basin.add(location)    // add it to the basin

        // find neighbours to add to the basin
        val next = neighbours
            .map { location + it}            // next neighbour
            .filter { !basin.contains(it)}   // skip neighbours already grown to the seed
            .filter { this.element(it) < 9 } // do not include basin edges

        open.addAll(next)
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