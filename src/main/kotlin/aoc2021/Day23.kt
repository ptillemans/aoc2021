package aoc2021

import com.google.gson.Gson
import io.ktor.util.*
import java.lang.System.lineSeparator
import java.util.*
import kotlin.Comparable as Comparable


/* maze

      0   1   2   3   4   5   6   7   8   9   10
             11      12      13      14
             15      16      17      18
             19      20      21      22    (*) optional
             23      24      25      26
*/

class Maze(val roomSize: Int = 2) {

    private val hallWay: List<Byte> = (0..10).map { it.toByte() }
    private val room = (hallWay.size until hallWay.size + 4 * roomSize).map { it.toByte() }

    private val doors = listOf(2, 4, 6, 8).map { it.toByte() }

    private val firstRoomPos = hallWay.size
    private val lastRoomPos = hallWay.size + 4 * roomSize

    private fun north(loc: Byte): Byte? =
        if (loc in (firstRoomPos + 4)..lastRoomPos)
            (loc - 4).toByte()
        else if (loc >= firstRoomPos)
            (2 + (loc - firstRoomPos) * 2).toByte()
        else
            null

    private fun south(loc: Byte): Byte? =
        if (loc in firstRoomPos until lastRoomPos - 4)
            (loc + 4).toByte()
        else if (loc in doors)
            (firstRoomPos + (loc - 2) / 2).toByte()
        else
            null

    private fun west(loc: Byte): Byte? =
        if (loc in 1 until firstRoomPos) (loc - 1).toByte() else null

    private fun east(loc: Byte): Byte? =
        if (loc in 0 until firstRoomPos - 1) (loc + 1).toByte() else null

    fun links(loc: Byte): List<Byte> =
        listOfNotNull(north(loc), south(loc), west(loc), east(loc))

    private fun isTargetRoom(amphipod: Int, loc: Byte): Boolean =
        (loc >= firstRoomPos)
                && amphipod / roomSize == (loc - firstRoomPos) % 4

    private fun isNoOtherAmphipodInRoom(state: State, amphipod: Int, loc: Byte): Boolean {
        val kind = amphipod / roomSize
        if (loc < firstRoomPos) {
            return true
        }
        val roomNumber = (loc - firstRoomPos) % 4
        val otherPods = (0 until roomSize). map { it*4 + firstRoomPos + roomNumber }
            .map { state.locations.indexOf(it.toByte()) }
            .filter { it > 0 }

        return otherPods.isEmpty() || otherPods.all { it/roomSize == kind }
    }

    private fun validMove(state: State, amphipod: Int, loc: Byte, newLoc: Byte): Boolean {
        if (newLoc in state.locations) {
            return false   // newLoc is occupied
        }

        if (loc in hallWay
            && newLoc in room
        ) {
            return isTargetRoom(amphipod, newLoc)                   // only enter target room
                    && isNoOtherAmphipodInRoom(state, amphipod, newLoc) // cannot enter if others are there
        }

        return true
    }

    fun isBottomPosition(state: State, amphipod: Int, loc: Byte): Boolean {
        val roomStart = amphipod / roomSize + firstRoomPos
        val locLower = (0 until roomSize)
            .map { roomStart + 4 * it }
            .filter { it > loc }

        if (locLower.isEmpty()) {
            return true
        }

        val result = locLower.all {
            val room = state.locations
                .slice((amphipod/roomSize) * roomSize until (amphipod / roomSize + 1) * roomSize)
            room.contains(it.toByte())
        }

        return result
    }

    private fun canStop(state: State, amphipod: Int, loc: Byte, newLoc: Byte): Boolean {
        if (newLoc in doors) {
            return false   // never stop at door
        }

        if (loc in hallWay && newLoc in hallWay) {
            return false   // cannot go from hallway to hallway
        }

        if (loc in room && newLoc in room) {
            return false   // cannot go from room to room
        }

        if (isTargetRoom(amphipod, newLoc) && !isBottomPosition(state, amphipod, newLoc)) {
            return false  // has to go to bottom most position
        }

        return true
    }

    private fun moveCost(amphipod: Int): Int =
        (0..amphipod / roomSize).fold(1) { cost, _ -> 10 * cost } / 10

    public fun allMoves(state: State, amphipod: Int): List<State> {

        val currentLocation = state.locations[amphipod]

        // do not bother for amphipods which are already in place
        if (isTargetRoom(amphipod, currentLocation)
            && isNoOtherAmphipodInRoom(state, amphipod, currentLocation)) {
            return listOf()
        }

        val seen = mutableSetOf<Byte>()
        val open = mutableListOf(Pair(currentLocation, state.cost))
        val stepCost = moveCost(amphipod)
        val moves = mutableSetOf<Pair<Byte, Int>>()
        while (open.isNotEmpty()) {
            val (loc, cost) = open.removeFirst()
            val newLocations =
                links(loc)
                    .filter { !seen.contains(it) }
                    .filter { validMove(state, amphipod, loc, it) }
            seen.addAll(newLocations)
            newLocations.map { Pair(it, cost + stepCost) }
                .forEach {
                    open.add(it)
                    moves.add(it)
                }
        }
        return moves
            .filter { canStop(state, amphipod, currentLocation, it.first) }
            .map {
                val newLocations = state.locations.toMutableList()
                newLocations[amphipod] = it.first
                State(newLocations, it.second)
            }
    }

    fun nextMoves(state: State): List<State> =
        state.locations.flatMapIndexed { amphipod, _ ->
            allMoves(state, amphipod)
        }

    private fun isSolved(state: State): Boolean {
        val atHome = state.locations
            .mapIndexed { idx, loc ->
                loc >= firstRoomPos && (loc - firstRoomPos) % 4 == idx / roomSize
            }
        return atHome.all { it }
    }


    fun solve(state: State): Int {

        val open = PriorityQueue<State>()
        val closed: MutableMap<List<Byte>, Int> = mutableMapOf()

        open.add(state)

        while (open.isNotEmpty()) {
            val curState = open.remove()
            if (isSolved(curState)) {
                return curState.cost
            }

            if (closed[curState.locations] ?: Int.MAX_VALUE <= curState.cost) {
                continue
            }
            closed[curState.locations] = curState.cost
            val nextStates = nextMoves(curState)
            open.addAll(nextStates)
        }

        return -1
    }
}


data class State(val locations: List<Byte>, val cost: Int) : Comparable<State> {

    fun nextMoves() {
        locations.map { loc ->

        }
    }

    override fun compareTo(other: State): Int {
        return this.cost.compareTo(other.cost)
    }


}

fun String.parseState(maze: Maze): State {

    var raw = this.split(lineSeparator())
        .drop(2)
        .map { it.replace(" ", "") }
        .map { it.replace("#", "") }
        .filter { it.isNotEmpty() }

    if (maze.roomSize > 2) {
        raw = listOf(
            raw[0],
            "DCBA",
            "DBAC",
            raw[1],
        )
    }

    val locations = raw.flatMapIndexed { lineNo, line ->
        line.split("").filter { it.isNotBlank() }
            .mapIndexed { roomNo, s -> 11 + roomNo + lineNo * 4 to s }
    }
        .sortedBy { it.second }
        .map { it.first.toByte() }

    return State(locations, 0)

}

class Day23 {

    var input: String

    init {
        val filename = "/aoc2021/day23/input.txt"
        input = Day23::class.java.getResource(filename).readText()
    }


    fun part1(): String {
        val maze = Maze(2)
        val state = input.parseState(maze)
        return maze.solve(state).toString()
    }


    fun part2(): String? {
        val maze = Maze(4)
        val state = input.parseState(maze)
        return maze.solve(state).toString()
    }

}


fun main() {
    val challenge = Day23()
    val solutions = mapOf(
        "part1" to challenge.part1(),
        "part2" to challenge.part2(),
    )
    println("Solutions: ")
    println(Gson().toJson(solutions))
}