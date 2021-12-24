package aoc2021

import java.util.*
import kotlin.math.abs



    val hallwayStops = listOf(0, 1, 3, 5, 7, 9, 10)
    val doors = listOf(2, 4, 6, 8)
    val room1 = listOf(11, 12, 13, 14)
    val room2 = listOf(21, 22, 23, 24)
    val room3 = listOf(31, 32, 33, 34)
    val room4 = listOf(41, 42, 43, 44)
    val rooms = listOf(room1, room2, room3, room4)
    val costs = listOf(1, 10, 100, 1000)

    data class StateB(val numNumbers: Int, val positions: List<Int>) {

        fun isFinished(): Boolean {
            return positions.chunked(numNumbers).mapIndexed { i, poses ->
                poses.all { it in rooms[i] }
            }.all { it }
        }

        fun getPossibleMoves(): List<Pair<Int, StateB>> {
            return positions.indices.flatMap { getMoves(it) }
        }

        private fun getMoves(amphi: Int): List<Pair<Int, StateB>> {
            val pos = positions[amphi]
            val goalRoom = amphi / numNumbers
            val goalRoomSpots = rooms[goalRoom]

            if (pos > 10) { // in a room
                if (pos in goalRoomSpots) { // don't make a move if in the correct spot
                    // and nothing behind me that should out
                    val range = (pos + 1)..((goalRoom + 1) * 10) + numNumbers
                    val blockedBehind = positions.filterIndexed { i, p -> p in range && i / numNumbers != goalRoom }
                    if (blockedBehind.isEmpty()) {
                        return listOf()
                    }
                }

                val potentialBlockages = (pos - (pos % 10) + 1) until pos
                val isBlocked = positions.any { it in potentialBlockages }
                if (isBlocked) { // Can't move out
                    return listOf()
                }


                val currentRoom = (pos / 10) - 1
                val entrance = doors[currentRoom]
                val stepToEntrance = pos % 10
                val cost = costs[goalRoom]

                // move to all possible hallway spots
                val moves = hallwayStops.mapNotNull { stop ->
                    if (canGoTo(to = stop, from = entrance)) {
                        val dst = abs(entrance - stop)
                        val totalCost = cost * (stepToEntrance + dst)
                        val newStateB = positions.toMutableList()
                        newStateB[amphi] = stop
                        totalCost to StateB(numNumbers, newStateB)
                    } else {
                        null
                    }
                }
                return moves
            } else { // outside
                val doorPos = doors[goalRoom]
                val canGoToDoor = canGoTo(pos, doorPos)
                if (!canGoToDoor) {
                    return listOf()
                }

                val anyoneNotSupposedInTheRoom = positions.filterIndexed { i, amphiPos ->
                    amphiPos in goalRoomSpots && i / numNumbers != goalRoom
                }
                if (anyoneNotSupposedInTheRoom.any()) { // Too early to go in
                    return listOf()
                }

                val numAlreadyThere = positions.count { it in goalRoomSpots }

                val stepsFromEntrance = numNumbers - numAlreadyThere
                val stepsToEntrance = abs(pos - doorPos)
                val finalPos = goalRoomSpots[numNumbers - numAlreadyThere - 1]
                val cost = costs[goalRoom]

                val totalCost = cost * (stepsToEntrance + stepsFromEntrance)

                val newStateB = positions.toMutableList()
                newStateB[amphi] = finalPos
                return listOf(totalCost to StateB(numNumbers, newStateB))
            }
        }

        private fun canGoTo(from: Int, to: Int): Boolean {
            val range = minOf(from + 1, to)..maxOf(from - 1, to)
            return !positions.any { pos -> pos in range }
        }

        fun heuristic(): Int {
            return positions.mapIndexed { i, pos ->
                val extraDst = i % numNumbers
                val goalRoom = i / numNumbers
                if (pos > 10) { // in a room
                    val currentRoom = (pos / 10) - 1
                    if (goalRoom == currentRoom) {
                        0
                    } else {
                        val dst = abs(doors[goalRoom] - doors[currentRoom]) + 1 + extraDst + (pos % 10)
                        costs[goalRoom] * dst
                    }
                } else { // outside
                    val dst = abs(doors[goalRoom] - pos) + 1 + extraDst
                    costs[goalRoom] * dst
                }
            }.sum()
        }
    }

    fun day23_1(lines: List<String>): Any {
        // Ex part1
//    val startStateB = listOf(room1[1], room4[1], room1[0], room3[0], room2[0], room3[1], room2[1], room4[0])
//    val startStateB = listOf(room1[1], room4[1], room1[0], 3, room2[0], room3[1], room2[1], room4[0])
//    val start = StateB(2, startStateB)

        // Part 1
//    val startStateB = listOf(room2[1], room4[0], room1[0], room2[0], room1[1], room4[1], room3[0], room3[1])
//    val start = StateB(2, startStateB)

        // Ex Part 2
//    val startStateB = listOf(
//        room1[3], room3[2], room4[1], room4[3],
//        room1[0], room2[2], room3[0], room3[1],
//        room2[0], room2[1], room3[3], room4[2],
//        room1[1], room1[2], room2[3], room4[0]
//    )
//    val start = StateB(4, startStateB)

        // Part 2
        val startStateB_ = listOf(
            room2[3], room3[2], room4[0], room4[1],
            room1[0], room2[0], room2[2], room3[1],
            room1[3], room2[1], room4[2], room4[3],
            room1[1], room1[2], room3[0], room3[3],
        )

        // "BABC"
        // "DCBA"
        // "DBAC"
        // "CDDA"
        val startStateB = listOf(
            21, 41, 42, 31,
            11, 32, 22, 43,
            23, 24, 12, 44,
            33, 13, 34, 14
        )
        val start = StateB(4, startStateB)


        val visited = mutableSetOf<StateB>()
        val queue = PriorityQueue<Triple<Int, StateB, Int>>(Comparator.comparing { it.first + it.third })
        queue.offer(Triple(0, start, start.heuristic()))

        var count = 0

        while (queue.isNotEmpty()) {
            count++
            val u = queue.poll()

            if (u.second.isFinished()) {
                println("Searched: $count")
                return u.first
            }
            if (u.second in visited) {
                continue
            }
            visited.add(u.second)

            u.second.getPossibleMoves().filterNot { it.second in visited }.forEach { (cost, state) ->
                val totalCost = u.first + cost
                queue.offer(Triple(totalCost, state, state.heuristic()))
            }

        }
        return "failed"

    }


    fun main() {
        day23_1(listOf())
    }