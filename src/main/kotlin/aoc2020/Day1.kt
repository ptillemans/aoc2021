package aoc2020

class Day1 {
    
    var input: String
    
    init {
        val filename = "/aoc2020/day1/input.txt"
        input = Day1::class.java.getResource(filename).readText()
    } 

    fun part1(): String? {
        return null
    }
   
    fun part2(): String?{
        return null
    }
    
    fun main() {
        println("Part1: ")
        println(part1())
        println("Part2: ")
        println(part2())
    }
}

fun main(args: Array<String>) {
    val challenge = Day1()
    challenge.main()
}