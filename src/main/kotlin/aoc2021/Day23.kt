package aoc2021

import com.google.gson.Gson
import common.IntMatrix
import common.neighbours
import io.ktor.util.*
import kotlinx.coroutines.newFixedThreadPoolContext
import java.lang.System.lineSeparator
import java.util.*
import javax.sound.sampled.BooleanControl
import kotlin.Comparable as Comparable

enum class AmphipodKind {
    Amber,
    Bronze,
    Copper,
    Desert
}

data class Amphipod(
    val name: String,
    val kind: AmphipodKind,
) {
    fun moveCost() =
        when (kind) {
            AmphipodKind.Amber -> 1
            AmphipodKind.Bronze -> 10
            AmphipodKind.Copper -> 100
            AmphipodKind.Desert -> 1000
        }

    override fun toString(): String = name
}

data class Maze(
    val cells: List<Cell>,
    val amphipods: List<Amphipod>,
    val roomSize: Int = 2
) {
    override fun toString(): String = cells.toString()
}

data class Cell(
    val name: String,
    var north: Cell? = null,
    var south: Cell? = null,
    var east: Cell? = null,
    var west: Cell? = null,
    val home: AmphipodKind? = null
) {
    fun links(): List<Cell> = listOfNotNull(north, east, south, west)

    fun isRoom(): Boolean = home != null
    fun isHallway(): Boolean = home == null

    override fun toString(): String = name

    override fun hashCode(): Int = name.hashCode()

}

data class State(val locations: Map<Amphipod, Cell>, val cost: Int) : Comparable<State> {

    fun isSolved() : Boolean {
        return locations.all { (a, c) -> a.kind == c.home}
    }

    override fun compareTo(other: State): Int =
       this.cost.compareTo(other.cost)
}


class Day23 {

    var input: String

    init {
        val filename = "/aoc2021/day23/input.txt"
        input = Day23::class.java.getResource(filename).readText()
    }


    fun part1(): String {
        val maze = generateMaze()
        val state = input.parseState(maze)
        return state.solve().toString()
    }


    fun part2(): String? {
        val maze = generateMaze()
        val state = input.parseState(maze)
        return state.solve().toString()
    }

}

fun generateMaze(roomSize: Int = 2): Maze {
    val hallway = (1..11)
        .map() { i -> Cell("H$i") }

    hallway
        .zipWithNext()
        .forEach { (a, b) ->
            a.east = b
        }
    hallway
        .reversed()
        .zipWithNext()
        .forEach { (a, b) ->
            a.west = b
        }

    val rooms = AmphipodKind.values()
        .flatMap { kind ->
            (1..roomSize)
                .map { i ->
                    Cell(name = "${kind.name.lowercase()}$i", home = kind)
                }
        }

    rooms
        .zipWithNext()
        .forEach { (u, d) ->
            if (u.home == d.home) {
                u.south = d
            }
        }

    rooms
        .reversed()
        .zipWithNext()
        .forEach { (d, u) ->
            if (u.home == d.home) {
                d.north = u
            }
        }

    (0..3).forEach { i ->
        hallway[2 + i * 2].south = rooms[i * roomSize]
        rooms[i * roomSize].north = hallway[2 + i * 2]
    }

    val amphipods = AmphipodKind.values()
        .flatMap { kind ->
            (0 until roomSize).map { i ->
                Amphipod("${kind.name}${i+1}", kind)
            }
        }

    return Maze(hallway + rooms, amphipods)

}

fun String.parseState(maze: Maze, part2: Boolean = false): State {

    var raw = this.split(lineSeparator())
        .drop(2)
        .map { it.replace(" ", "") }
        .map { it.replace("#", "") }
        .filter { it.isNotEmpty() }

    if (part2) {
        raw = listOf(
            raw[0],
            "DCBA",
            "DBAC",
            raw[1],
        )
    }

    val remaining = maze.amphipods.toMutableList()
    val locations = raw.flatMapIndexed { row, line ->
        line.map { c ->
            val amphipod = remaining.first { it.kind.name.startsWith(c.toString()) }
            remaining.remove(amphipod)
            amphipod
            }
            .mapIndexed { i, amphipod ->
                amphipod to maze.cells[11 + row + maze.roomSize*i]
            }
    }
        .toMap()

    return State(locations,0)
}


fun State.inPlace(amphipod: Amphipod): Boolean {
    val curLoc = this.locations[amphipod]!!
    if (curLoc.home != amphipod.kind) {
        return false
    }
    return generateSequence(amphipod) { a ->
        val c = this.locations[a]?.south
        this.locations.filter { (_, v) -> v == c }.map { it.key }.first()
    }
        .all { it.kind == amphipod.kind }
}

fun State.nextStatesForAmphipod(amphipod: Amphipod): List<State> {

    val seen = mutableSetOf<Cell>()
    val open = this.locations[amphipod]!!.links()
        .map { loc -> Pair(loc, this.cost+amphipod.moveCost()) }
        .toMutableList()
    val states = mutableListOf<State>()
    val curLoc = this.locations[amphipod]!!

    if (this.inPlace(amphipod)) {
        return listOf()
    }

    while (open.isNotEmpty()) {
        val (newLoc, newCost) = open.removeFirst()
        if (newLoc in seen || newCost > 60000) {
            continue
        }
        seen.add(newLoc)

        // new location is occupied
        if (newLoc in this.locations.values) {
            continue
        }

        // new location is the wrong kind of room
        if (curLoc.isHallway() && newLoc.isRoom() && newLoc.home != amphipod.kind) {
            continue
        }


        open.addAll(newLoc.links()
            .filter {it !in seen}
            .map { Pair(it, newCost + amphipod.moveCost())})

        // connot move from room to room
        if (newLoc.isRoom() && curLoc.isRoom()) {
            continue
        }

        // cannot move from hallway to hallway
        if (newLoc.isHallway() && curLoc.isHallway()) {
            continue
        }

        // cannot move to room if it contains other kinds
        if (newLoc.isRoom() && locations.any { (a, c) -> c.home == newLoc.home && a.kind != amphipod.kind }) {
            continue
        }

        // cannot stop in hallway above room {
        if (newLoc.isHallway() && newLoc.south != null) {
            continue
        }

        // cannot stop in room if there is open space below
        if (newLoc.isRoom() && newLoc.south != null && newLoc.south !in this.locations.values) {
            continue
        }

        val newState = State(this.locations.toMutableMap() + (amphipod to newLoc), newCost)
        states.add(newState)
    }

    return states
}

fun State.nextStates(): List<State> =
    this.locations.keys
        .flatMap { this.nextStatesForAmphipod(it)}

fun State.solve ( ): Int {
    val open = PriorityQueue<State>()
    val closed: MutableMap<Map<Amphipod, Cell>, Int> = mutableMapOf()

    open.addAll(this.nextStates())

    var cnt = 0
    var state: State = this
    while (open.isNotEmpty()) {
        // take next location from the unprocessed locations
        state = open.remove()
        if (state.isSolved()) {
            break
        }

        if (closed[state.locations]?:Int.MAX_VALUE < state.cost) {
            continue
        }

        closed[state.locations] = state.cost

        if (cnt % 1000 == 0) {
            println("${state.cost}: ${open.size} - ${closed.size}")
        }
        cnt += 1

        open.addAll(state.nextStates())
    }

    return state.cost
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