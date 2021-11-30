package aoc2020

class Day1 {
    
    
    fun getInput(): String {
        val filename = "/aoc2020/day1/input.txt"
        return Day1::class.java.getResource(filename).readText()
    } 

    fun part1(): Int? {
        return null
    }
   
    fun part2(): Int?{
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