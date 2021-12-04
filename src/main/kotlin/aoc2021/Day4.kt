package aoc2021

import com.google.gson.Gson
import common.splitOn
import common.toIntList
import common.toIntMatrix

typealias Board = List<List<Int>>

class Day4 {
    
    var input: List<String>
    
    init {
        val filename = "/aoc2021/day4/input.txt"
        input = Day4::class.java.getResource(filename)
            .readText()
            .split("\n")
    }
    

    fun part1(): String {
        val (randomBalls, boards) = parseInput(input)
        val score = randomBalls.indices
            .map {randomBalls.slice(0 until it)}
            .flatMap{ balls ->
                boards
                    .filter { it.hasWon(balls) }
                    .map {it.score(balls)}}
            .first()
        return score.toString()
    }
   
    fun part2(): String? {
        val (randomBalls, boards) = parseInput(input)
        val winningBoards = randomBalls.indices
            .map {randomBalls.slice(0 until it)}
            .flatMap{ balls ->
                boards
                    .filter { it.hasWon(balls) }
                    .map {Pair(it, it.score(balls))}}
        var boardsInPlay = boards.toMutableSet()
        for ((board, score) in winningBoards) {
            if (!boardsInPlay.contains(board)) {
                continue
            }
            boardsInPlay.remove(board)
            if (boardsInPlay.isEmpty()) {
                return score.toString()
            }
        }
        return null
    }

    fun parseInput(lines: List<String>): Pair<List<Int>, List<Board>> {
        val groups = lines.splitOn { it.isEmpty() }
        return Pair(
            groups.first().first().toIntList(),
            groups.drop(1).filter { it.isNotEmpty() }.map { it.toIntMatrix() }
        )
    }
}

fun Board.hasWon(balls: List<Int>): Boolean {
    val rowWins = this
        .map {row: List<Int> -> row.all { balls.contains(it) }}
        .any {it}
    val colWins = (0 until this[0].size)
        .map { col -> this.map { it[col]}.all { balls.contains(it)}}
        .any {it}
    return rowWins || colWins
}

fun Board.score(balls: List<Int>): Int {
    val lastBall = balls.last()
    val unmarkedNumbers = this
        .flatMap { it.filter {!balls.contains(it)}}
    return lastBall * unmarkedNumbers.sum()
}


fun main() {
    val challenge = Day4()
    val solutions = mapOf (
        "part1" to challenge.part1(),
        "part2" to challenge.part2(),
    )
    println("Solutions: ")
    println(Gson().toJson(solutions))
}