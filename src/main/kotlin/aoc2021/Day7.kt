package aoc2021

import com.google.gson.Gson
import common.toIntList
import java.lang.Math.abs

class Day7 {
    
    var input: List<Int>
    
    init {
        val filename = "/aoc2021/day7/input.txt"
        input = Day7::class.java.getResource(filename).readText().toIntList()
    }
    

    fun part1(): String? {
        return input.findOptimum().second.toString()
    }

    fun part2(): String? {
        return input.findOptimum(fuelConsumption2).second.toString()
    }
    
}

val fuelConsumption : List<Int>.(Int) -> Int = { pos -> this.map{ abs(it - pos)}.sum() }
val fuelConsumption2 : List<Int>.(Int) -> Int = { pos -> this.map{ x -> abs(x - pos).let { it*(it+1)/2}}.sum() }

fun List<Int>.findOptimum(fuel: List<Int>.(Int) -> Int = fuelConsumption):Pair<Int, Int> {
    var minX=this.minOrNull()?:0
    var minF=this.fuel(minX)
    var maxX = this.maxOrNull()?:0
    var maxF=this.fuel(maxX)
    var step = 1
    while (step < (maxX - minX)/2) {
        step = step * 2
    }
    while (maxX > minX + 1) {
        val midX = (maxX + minX)/2
        val midFuel = this.fuel(midX)
        if (maxF > minF) {
            maxX = midX
            maxF=midFuel
        } else {
            minX = midX
            minF=midFuel
        }
    }
    if (maxF < minF) {
        return Pair(maxX, maxF)
    } else {
        return Pair(minX, minF)
    }
}


fun main(args: Array<String>) {
    val challenge = Day7()
    val solutions = mapOf (
        "part1" to challenge.part1(),
        "part2" to challenge.part2(),
    )
    println("Solutions: ")
    println(Gson().toJson(solutions))
}