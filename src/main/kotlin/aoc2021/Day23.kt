package aoc2021

import com.google.gson.Gson 

class Day23 {
    
    var input: String
    
    init {
        val filename = "/aoc2021/day23/input.txt"
        input = Day23::class.java.getResource(filename).readText()
    }
    

    fun part1(): String? {
        return null
    }
   
    fun part2(): String? {
        return null
    }
    
}

fun main() {
    val challenge = Day23()
    val solutions = mapOf (
        "part1" to challenge.part1(),
        "part2" to challenge.part2(),
    )
    println("Solutions: ")
    println(Gson().toJson(solutions))
}