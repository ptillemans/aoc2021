package aoc2021

import com.google.gson.Gson
import java.lang.Long.max

data class Player(var pos: Int, var throws: Int = 0, var score: Int = 0) {
    fun play(die: DeterministicDie): Boolean {
        throws += 3
        val moves = die.throwDie() + die.throwDie() + die.throwDie()
        pos = (pos - 1 + moves) % 10 + 1
        score += pos
        if (hasWon()) {
            return true
        }
        return false
    }

    fun hasWon(): Boolean = score >= 1000
}

data class DeterministicDie(var n: Int = 0) {
    fun throwDie(): Int {
        n = n % 100 + 1;
        return n
    }
}

fun playGame(p1: Int, p2: Int): Pair<Player, Player> {
    val players = listOf(
        Player(p1),
        Player(p2),
    )
    var round = 0
    val die = DeterministicDie()
    while (!players[round % 2].play(die)) {
        round += 1
    }
    return Pair(players[0], players[1])
}

data class DiracNode(val player: Player, val amount: Int, val children: List<DiracNode>) {

}

data class DiracPlayer(val pos: Int, val score: Int=0) {
    fun hasWon() = score >= 21

    fun move(n: Int): DiracPlayer {
        val np = (pos - 1 + n) % 10 + 1
        val sc = score + np
        return DiracPlayer(np, sc)
    }

    fun play(): List<Pair<Long, DiracPlayer>> = listOf(
        Pair(1, move(3)),
        Pair(3, move(4)),
        Pair(6, move(5)),
        Pair(7, move(6)),
        Pair(6, move(7)),
        Pair(3, move(8)),
        Pair(1, move(9)),
    )
}

operator fun Long.times(p: Pair<Long, Long>): Pair<Long,Long> = Pair(p.first*this, p.second*this)

operator fun Pair<Long, Long>.plus(p: Pair<Long, Long>) : Pair<Long, Long> =
    Pair(this.first + p.first, this.second + p.second)

fun doRound(p1: DiracPlayer, p2: DiracPlayer, p1Plays: Boolean): Pair<Long, Long> =
    if (p1Plays) {
        p1.play()
            .map { (n, p) -> if (p.hasWon()) Pair(n, 0L) else n * doRound(p, p2, !p1Plays) }
            .reduce { acc, x -> acc + x}
    } else {
        p2.play()
            .map { (n, p) -> if (p.hasWon()) Pair(0L, n) else n * doRound(p1, p, !p1Plays) }
            .reduce { acc, x -> acc + x}
    }


fun playDirac(p1: Int, p2: Int): Pair<Long, Long> =
    doRound(DiracPlayer(p1), DiracPlayer(p2), true)



class Day21 {

    var input = Pair(1, 2)

    fun part1(): String? {
        val (p1, p2) = playGame(input.first, input.second)

        val totThrows = p1.throws + p2.throws
        val result = if (p1.hasWon())
            p2.score * totThrows
        else
            p1.score * totThrows
        return result.toString()
    }

    fun part2(): String? {
        val (n1, n2) = playDirac(input.first, input.second)

        return max(n1, n2).toString()
    }

}

fun main() {
    val challenge = Day21()
    val solutions = mapOf(
        "part1" to challenge.part1(),
        "part2" to challenge.part2(),
    )
    println("Solutions: ")
    println(Gson().toJson(solutions))
}