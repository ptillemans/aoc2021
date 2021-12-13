package aoc2021

import com.google.gson.Gson 

typealias Node = String
typealias Edge = Pair<Node, Node>
typealias Path = List<Node>

val START_NODE = "start"
val END_NODE = "end"

class Day12 {
    
    var input: String
    
    init {
        val filename = "/aoc2021/day12/input.txt"
        input = Day12::class.java.getResource(filename)!!.readText()
    }
    

    fun part1(): String =
        input.parseMaze()
            .generatePaths(canVisit = ::canVisitPart1)
            .count()
            .toString()
   
    fun part2(): String =
        input.parseMaze()
            .generatePaths(canVisit = ::canVisitPart2)
            .count()
            .toString()

}

fun String.parseMaze() =
    this.split(System.lineSeparator())
        .filter { it.isNotEmpty() }
        .map {line ->
            line.split("-")
            .zipWithNext()
            .first()
        }

private fun Edge.otherNode(node:Node) =
    if (this.first == node) this.second else this.first

private fun Node.isLargeCave() =
    this.all { it.isUpperCase()}

fun canVisitPart1(seen: Map<Node, Int>, n: Node): Boolean =
    n.isLargeCave() || !seen.contains(n)


fun canVisitPart2(seen: Map<Node, Int>, n: Node): Boolean =
    n.isLargeCave() || !seen.contains(n)
    || seen.all { (k, v) -> k.isLargeCave() || v <= 1 }

fun List<Edge>.generatePaths(
    node: Node = START_NODE,
    seen: Map<Node,Int> = mapOf(),
    canVisit: Map<Node,Int>.(Node) -> Boolean = ::canVisitPart1)
: List<Path> =
    filter { it.toList().contains(node) }
        .map { it.otherNode(node) }
        .filter { it != START_NODE }
        .filter{ seen.canVisit(it) }
        .flatMap { n ->
            if (n == END_NODE)
                listOf(listOf(node, n))
            else
                this.generatePaths(n, seen + (n to (seen[n]?:0) + 1), canVisit)
                    .map { path -> listOf(node) + path }

        }


fun main() {
    val challenge = Day12()
    val solutions = mapOf (
        "part1" to challenge.part1(),
        "part2" to challenge.part2(),
    )
    println("Solutions: ")
    println(Gson().toJson(solutions))
}