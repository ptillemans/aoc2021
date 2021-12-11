package aoc2020

import com.google.gson.Gson 

class Day3 {
    
    var input: String
    
    init {
        val filename = "/aoc2020/day3/input.txt"
        input = Day3::class.java.getResource(filename).readText()
    }
    

    fun part1(): String? {
        return null
    }
   
    fun part2(): String? {
        return null
    }
    
}

fun main() {
    val challenge = Day3()
    val solutions = mapOf (
        "part1" to challenge.part1(),
        "part2" to challenge.part2(),
    )
    println("Solutions: ")
    println(Gson().toJson(solutions))
}