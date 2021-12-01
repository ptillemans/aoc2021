package aoc2021

import com.google.gson.Gson 

class Day2 {
    

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