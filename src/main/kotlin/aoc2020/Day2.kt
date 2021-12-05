package aoc2020

import aoc2021.Command
import aoc2021.Direction
import com.google.gson.Gson

class Day2 {
    
    var input: String
    
    init {
        val filename = "/aoc2020/day2/input.txt"
        input = Day2::class.java.getResource(filename).readText()
    }
    

    fun part1(): String? {
        return null
    }
   
    fun part2(): String? {
        return null
    }


}

fun main() {
    val challenge = Day2()
    val solutions = mapOf (
        "part1" to challenge.part1(),
        "part2" to challenge.part2(),
    )
    println("Solutions: ")
    println(Gson().toJson(solutions))
}