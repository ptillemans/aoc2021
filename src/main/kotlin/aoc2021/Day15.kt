package aoc2021

import com.google.gson.Gson
import common.*
import java.util.*

class Day15 {
    
    var input: IntMatrix

    init {
        val filename = "/aoc2021/day15/input.txt"
        input = Day15::class.java.getResource(filename)!!.readText().toIntMatrix()
    }
    

    fun part1(): String {
        val path = input.findPath()
        return path.first().cost.toString()
    }

   
    fun part2(): String {
        val path = input.expandMap().findPath()
        return path.first().cost.toString()
    }
    
}
data class Day15Node(val cost: Int, val position: Point, val previous: Day15Node? = null): Comparable<Day15Node> {
    override fun compareTo(other: Day15Node): Int =
        cost.compareTo(other.cost)
}


fun manhattan(map: IntMatrix, n: Day15Node): Int {
    val endPoint = Point(map.nColumns-1, map.nRows-1)
    val dist =  (endPoint - n.position).toList()
    return dist.sum()
}

fun canVisit(map: IntMatrix, n: Day15Node): Boolean = map.get(n.position) < 10

fun costFunction(map: IntMatrix, n: Day15Node): Int = n.cost

fun IntMatrix.findPath(
    canVisit: IntMatrix.(Day15Node) -> Boolean = ::canVisit,
    g: IntMatrix.(Day15Node) -> Int = ::costFunction,
    h: IntMatrix.(Day15Node) -> Int = ::manhattan
): List<Day15Node> {
    val open = PriorityQueue<Day15Node>()
    val closed: MutableMap<Point, Int> = mutableMapOf()

    // put the first seed of the basin
    val startPoint = Point(0, 0)
    val endPoint = Point(this.nColumns-1, this.nRows-1)
    open.add(Day15Node(cost=0, position = startPoint))

    // let the basin grow around the seed
    var location: Day15Node = open.peek()
    while (open.isNotEmpty()) {
        // take next location from the unprocessed locations
        location = open.remove()
        if (location.position == endPoint) {
            break
        }
        closed[location.position] = this.g(location) + this.h(location)

        // find neighbours to add to the basin
        val next = neighbours
            .map { location.position + it}            // next neighbour
            .map { Day15Node(g(location) + this.get(it), it, location) }
            .filter { this.canVisit(it)}
            .map { Pair( it, this.g(it) + this.h(it))}
            .filter { it.second < (closed[it.first.position]?:Int.MAX_VALUE) }   // skip neighbours with shorter path
            .map { it.first }
            .filter { node -> open.find { it.position == node.position } == null }

        open.addAll(next)
    }

    return generateSequence(location) { it.previous }.toList()
}

fun Int.addCost(cost: Int) = (this - 1 + cost) % 9 + 1

fun IntMatrix.expandMap(): IntMatrix =
    IntMatrix(nRows * 5, List<Int>(5*5*this.nRows*this.nColumns) {
        val repX = (it / nColumns) % 5
        val repY = (it / (nRows * nColumns * 5))
        val x = it % nColumns
        val y = (it / (nColumns*5)) % nRows
        val v = this.get(x, y).addCost(repX + repY)
        v
    })


fun main() {
    val challenge = Day15()
    val solutions = mapOf (
        "part1" to challenge.part1(),
        "part2" to challenge.part2(),
    )
    println("Solutions: ")
    println(Gson().toJson(solutions))
}