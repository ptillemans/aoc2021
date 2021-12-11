package aoc2021

import com.google.gson.Gson
import common.IntMatrix
import common.toIntMatrix

class Day11 {
    
    var input: IntMatrix
    
    init {
        val filename = "/aoc2021/day11/input.txt"
        input = Day11::class.java.getResource(filename).readText().toIntMatrix()
    }
    

    fun part1(): String =
        iterateOctopusStates(input, 100)
            .second
            .toString()

   
    fun part2(): String =
        input.cyclesTillAllFlash().toString()
    
}

fun iterateOctopusStates(initial: IntMatrix, n:Int):Pair<IntMatrix, Int> =
    (1..n).fold(Pair(initial, 0)) { state, _ -> state.nextOctopusState() }

fun Pair<IntMatrix, Int>.nextOctopusState(): Pair<IntMatrix, Int> =
    this.first.increaseEnergy().handleFlashes(this.second)

fun IntMatrix.increaseEnergy() =
    IntMatrix(this.nRows, this.elements().map { it + 1})

fun IntMatrix.handleFlashes(oldFlashes: Int = 0): Pair<IntMatrix, Int> {
    val hasFlashed : MutableSet<Pair<Int, Int>> = mutableSetOf()
    var result = this
    while (result.elements().any { it > 9 }) {
        val newResult = result
            .entries()
            .filter { (pos, v) -> v > 9 && !hasFlashed.contains(pos) }
            .fold(result) { res, (pos, _) ->
                hasFlashed.add(pos)
                res.flash(pos, hasFlashed)
            }
        result = newResult
    }
    return Pair(result, hasFlashed.size + oldFlashes)
}

fun IntMatrix.flash(pos: Pair<Int, Int>, hasFlashed: Set<Pair<Int, Int>>): IntMatrix =
    pos.let { (x, y) ->
        (-1 .. 1)
            .flatMap { dx -> (-1..1).map { dy -> Pair(x + dx, y + dy) }}
            .fold(this) { acc, p ->
                if (p == pos || hasFlashed.contains(p))
                    acc.set(p, 0)
                else
                    acc.set(p, acc.get(p)+1)
            }
    }

fun IntMatrix.cyclesTillAllFlash(): Int =
    generateSequence(Pair(this,0)) { Pair(it.first, 0).nextOctopusState() }
        .takeWhile { it.second != it.first.body.size }
        .count()

fun main() {
    val challenge = Day11()
    val solutions = mapOf (
        "part1" to challenge.part1(),
        "part2" to challenge.part2(),
    )
    println("Solutions: ")
    println(Gson().toJson(solutions))
}