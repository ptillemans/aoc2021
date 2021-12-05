package aoc2021

import com.google.gson.Gson

enum class Direction {
    FORWARD,
    UP,
    DOWN
}

data class Command(val direction: Direction, val amount: Int)

data class Position(val x: Int, val depth: Int, val aim: Int = 0)

class Day2 {

    var input: String

    init {
        val filename = "/aoc2021/day2/input.txt"
        input = Day2::class.java.getResource(filename).readText()
    }


    fun parseInput(text: String): List<Command> {
        return text.split("\n")
            .filter { it.isNotEmpty() }
            .map { it.toCommand() }
    }

    fun part1(): String? =
        parseInput(input)
            .calculateCartesianPosition()
            .formatOutput()


    fun part2(): String? =
        parseInput(input)
            .calculateAimedPosition()
            .formatOutput()

}

fun String.toCommand(): Command {
    val parts = this.split(' ')
    val direction = Direction.valueOf(parts[0].uppercase())
    val amount = parts[1].toInt()
    return Command(direction, amount)
}

fun Command.executeCartesian(pos: Position) : Position =
    when (direction) {
        Direction.FORWARD -> Position(pos.x + amount, pos.depth)
        Direction.DOWN -> Position(pos.x, pos.depth + amount)
        Direction.UP -> Position(pos.x, pos.depth - amount)
    }

fun Command.executeAimed(pos: Position) : Position =
    when (direction) {
        Direction.FORWARD -> Position(pos.x + amount, pos.depth + pos.aim*amount, pos.aim)
        Direction.DOWN -> Position(pos.x, pos.depth, pos.aim + amount)
        Direction.UP -> Position(pos.x, pos.depth, pos.aim - amount)
    }

fun List<Command>.calculateCartesianPosition(start: Position = Position(0, 0)) : Position =
    this.fold(start) { pos, command -> command.executeCartesian(pos) }

fun List<Command>.calculateAimedPosition(start: Position = Position(0, 0)) : Position =
    this.fold(start) { pos, command -> command.executeAimed(pos) }

fun Position.formatOutput() : String = (x * depth).toString()

fun main() {
    val challenge = Day2()
    val solutions = mapOf(
        "part1" to challenge.part1(),
        "part2" to challenge.part2(),
    )
    println("Solutions: ")
    println(Gson().toJson(solutions))
}