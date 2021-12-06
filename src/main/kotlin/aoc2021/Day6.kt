package aoc2021

import com.google.gson.Gson
import common.toIntList
import java.math.BigInteger

typealias PopulationFrequency = Map<Int, BigInteger>

val ZERO = BigInteger.valueOf(0)

class Day6 {

    var input: List<Int>
    
    init {
        val filename = "/aoc2021/day6/input.txt"
        input = Day6::class.java.getResource(filename).readText().trim().toIntList()
    }
    

    fun part1(): String =
        (0 until 80)
            .fold(input.toPopulationFrequency()){ acc, _ -> acc.newGeneration()}
            .map { it.value }
            .reduce { acc, x -> acc + x}
            .toString()


    fun part2(): String =
        (0 until 256)
            .fold(input.toPopulationFrequency()){ acc, _ -> acc.newGeneration()}
            .map { it.value }
            .reduce { acc, x -> acc + x}
            .toString()

}

fun PopulationFrequency.updateTimers(): PopulationFrequency {
    val pop = this.map { (it.key - 1) to it.value}.toMap().toMutableMap()
    pop[6] = (pop[6]?: ZERO) + (pop[-1]?: ZERO)
    pop[8] = ZERO
    pop.remove(-1)
    return pop.toMap()
}

fun PopulationFrequency.newBorns(): BigInteger = this[0]?:BigInteger.valueOf(0)

fun PopulationFrequency.newGeneration(): PopulationFrequency{
    var population = this.updateTimers().toMutableMap()
    population[8] = this.newBorns()
    return population
}

fun List<Int>.toPopulationFrequency():PopulationFrequency {
    val pop = (0..8).associateWith { ZERO }.toMutableMap()
    for ((k,v) in this.sorted().groupBy { it }) {
         pop[k]=BigInteger.valueOf(v.size.toLong())
    }
    return pop.toMap()
}


fun main(args: Array<String>) {
    val challenge = Day6()
    val solutions = mapOf (
        "part1" to challenge.part1(),
        "part2" to challenge.part2(),
    )
    println("Solutions: ")
    println(Gson().toJson(solutions))
}