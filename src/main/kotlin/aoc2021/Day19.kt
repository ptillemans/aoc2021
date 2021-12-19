package aoc2021

import com.google.gson.Gson
import common.toIntList
import java.lang.System.lineSeparator

enum class Rotation {
    Rot0,
    Rot90,
    Rot180,
    Rot270
}

data class Coordinate(val x:Int, val y:Int, val z:Int) {

    fun distance(b: Coordinate) : Int {
        val dx = b.x - this.x
        val dy = b.y - this.y
        val dz = b.z - this.z
        return dx*dx + dy*dy + dz*dz
    }

}

data class Orientation(val xAxis: Rotation, val yAxis: Rotation, val zAxis: Rotation)

data class Scanner(val location: Coordinate?, val orientation: Orientation?, val beacons: List<Coordinate>)

class Day19 {
    
    var input: String
    
    init {
        val filename = "/aoc2021/day19/input.txt"
        input = Day19::class.java.getResource(filename).readText()
    }

    fun part1(): String? {
        return null
    }
   
    fun part2(): String? {
        return null
    }
    
}

fun String.toScanners() : List<Scanner> =
    this.split(lineSeparator() + lineSeparator())
        .filter { isNotBlank() }
        .map { line ->
            line.split(lineSeparator())
                .drop(1)
                .filter { it.isNotBlank() }
                .map { it.toIntList() }
                .map { Coordinate(x = it[0], y = it[1], z = it[2]) }
        }
        .map { Scanner(null, null, it)  }
        .mapIndexed { i, scanner -> if (i == 0)
            Scanner(
                Coordinate(0,0,0),
                Orientation(Rotation.Rot0, Rotation.Rot0, Rotation.Rot0),
                scanner.beacons
            )
        else
            scanner
        }

data class BeaconDistance(
    val scanner: Scanner
    val beacons: Set<Coordinate>
    val distance: Int
)

fun List<Coordinate>.toDistances(scanner: Scanner): List<BeaconDistance> =
    (0 until this.size).flatMap { i1 ->
        (i1+1 until this.size).map { i2 ->
            Pair(this[i1], this[i2])
        }
    }
        .map { BeaconDistance(scanner, setOf(it.first, it.second), it.first.distance(it.second)) }


fun List<BeaconDistance>.intersection(other: List<BeaconDistance>): List<Pair<BeaconDistance, BeaconDistance>> =
    this.flatMap { beacon ->
        other.filter { it.distance == beacon.distance }
            .map { Pair(beacon, it) }
    }


fun main() {
    val challenge = Day19()
    val solutions = mapOf (
        "part1" to challenge.part1(),
        "part2" to challenge.part2(),
    )
    println("Solutions: ")
    println(Gson().toJson(solutions))
}