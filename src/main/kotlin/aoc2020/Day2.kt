package aoc2020

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

fun main(args: Array<String>) {
    val challenge = Day2()
    val solutions = mapOf (
        "part1" to challenge.part1(),
        "part2" to challenge.part2(),
    )
    println("Solutions: ")
    println(Gson().toJson(solutions))
}