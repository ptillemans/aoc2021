package aoc2021

import com.google.gson.Gson 

class Day25 {
    
    var input: String
    
    init {
        val filename = "/aoc2021/day25/input.txt"
        input = Day25::class.java.getResource(filename).readText()
    }
    

    fun part1(): String? {
        return null
    }
   
    fun part2(): String? {
        return null
    }
    
}

fun main() {
    val challenge = Day25()
    val solutions = mapOf (
        "part1" to challenge.part1(),
        "part2" to challenge.part2(),
    )
    println("Solutions: ")
    println(Gson().toJson(solutions))
}