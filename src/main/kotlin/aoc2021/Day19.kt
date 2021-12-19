package aoc2021

import com.google.gson.Gson
import common.toIntList
import java.lang.Math.abs
import java.lang.System.`in`
import java.lang.System.lineSeparator
import java.util.*

enum class Rotation {
    Rot0,
    Rot90,
    Rot180,
    Rot270
}

data class Coordinate(val x: Int, val y: Int, val z: Int) {

    fun distance(b: Coordinate): Int {
        val dx = b.x - this.x
        val dy = b.y - this.y
        val dz = b.z - this.z
        return dx * dx + dy * dy + dz * dz
    }

    fun manhattan(b: Coordinate): Int {
        val dx = b.x - this.x
        val dy = b.y - this.y
        val dz = b.z - this.z
        return abs(dx) + abs(dy) + abs(dz)
    }

    operator fun plus(other: Coordinate): Coordinate =
        Coordinate(this.x + other.x, this.y + other.y, this.z + other.z)

    operator fun minus(other: Coordinate): Coordinate =
        Coordinate(this.x - other.x, this.y - other.y, this.z - other.z)
}

data class Orientation(val xAxis: Rotation, val yAxis: Rotation, val zAxis: Rotation) {
    fun rotate(c: Coordinate): Coordinate {
        return rotateX(rotateY(rotateZ(c)))
    }

    private fun rotateX(c: Coordinate): Coordinate =
        when (this.xAxis) {
            Rotation.Rot0 -> c
            Rotation.Rot90 -> Coordinate(c.x, -c.z, c.y)
            Rotation.Rot180 -> Coordinate(c.x, -c.y, -c.z)
            Rotation.Rot270 -> Coordinate(c.x, c.z, -c.y)
        }

    private fun rotateY(c: Coordinate): Coordinate =
        when (this.yAxis) {
            Rotation.Rot0 -> c
            Rotation.Rot90 -> Coordinate(-c.z, c.y, c.x)
            Rotation.Rot180 -> Coordinate(-c.x, c.y, -c.z)
            Rotation.Rot270 -> Coordinate(c.z, c.y, -c.x)
        }

    private fun rotateZ(c: Coordinate): Coordinate =
        when (this.zAxis) {
            Rotation.Rot0 -> c
            Rotation.Rot90 -> Coordinate(-c.y, c.x, c.z)
            Rotation.Rot180 -> Coordinate(-c.x, -c.y, c.z)
            Rotation.Rot270 -> Coordinate(c.y, -c.x, c.z)
        }

}

val allOrientations = findAllOrientations()

fun findAllOrientations(): List<Orientation> {
    val testVector = Coordinate(1, 2, 3)
    val seen = mutableSetOf<Coordinate>()
    val orientations = Rotation.values().flatMap { rotX ->
        Rotation.values().flatMap { rotY ->
            Rotation.values().map { rotZ -> Orientation(rotX, rotY, rotZ) }
        }
    }

    var result = mutableListOf<Orientation>()
    for (o in orientations) {
        val vec = o.rotate(testVector)
        if (!seen.contains(vec)) {
            result.add(o)
            seen.add(vec)
        }
    }

    return result
}

data class Scanner(val name: String, var location: Coordinate?, var orientation: Orientation?, val beacons: List<Coordinate>) {
    fun beaconLocations(): List<Coordinate> =
        this.beacons
            .map { c -> this.relToAbs(c) }

    fun relToAbs(c: Coordinate): Coordinate =
        this.orientation!!.rotate(c).plus(this.location!!)

}

class Day19 {

    var input: String

    init {
        val filename = "/aoc2021/day19/input.txt"
        input = Day19::class.java.getResource(filename).readText()
    }

    fun part1(): String =
        input
            .toScanners()
            .findAllBeacons()
            .size
            .toString()


    fun part2(): String =
        input
            .toScanners()
            .findLargestDifference()
            .toString()

}

fun String.toScanners(): List<Scanner> =
    this.split(lineSeparator() + lineSeparator())
        .filter { isNotBlank() }
        .map { line ->
            Pair(
                line.split(lineSeparator())
                    .first()
                    .replace("---","")
                    .trim(),
                line.split(lineSeparator())
                    .drop(1)
                    .filter { it.isNotBlank() }
                    .map { it.toCoordinate() }
            )
        }
        .map { Scanner(it.first, null, null, it.second) }
        .mapIndexed { i, scanner ->
            if (i == 0)
                Scanner(
                    scanner.name,
                    Coordinate(0, 0, 0),
                    Orientation(Rotation.Rot0, Rotation.Rot0, Rotation.Rot0),
                    scanner.beacons
                )
            else
                scanner
        }

fun String.toCoordinate(): Coordinate {
    val coords = this.toIntList()
    return Coordinate(x = coords[0], y = coords[1], z = coords[2])

}

data class BeaconDistance(
    val beacons: Set<Coordinate>,
    val distance: Int
)

fun Scanner.toDistances(): List<BeaconDistance> =
    (0 until this.beacons.size).flatMap { i1 ->
        (i1 + 1 until this.beacons.size).map { i2 ->
            BeaconDistance(
                setOf(this.beacons[i1], this.beacons[i2]),
                this.beacons[i1].distance(this.beacons[i2])
            )
        }
    }

fun List<BeaconDistance>.intersection(other: List<BeaconDistance>): List<Pair<BeaconDistance, BeaconDistance>> =
    this.flatMap { beacon ->
        other.filter { it.distance == beacon.distance }
            .map { Pair(beacon, it) }
    }

fun List<Pair<BeaconDistance, BeaconDistance>>.overlappingBeaconsInDistList(): Map<Coordinate, Set<Coordinate>> {
    if (this.size == 0)
        return mapOf()

    val distMap = this.associateBy { it.first.distance }

    val firstBeacons = this.map { it.first.beacons }
        .reduce { acc, bs -> acc.union(bs) }

    val secondBeacons = this.map { it.second.beacons }
        .reduce { acc, bs -> acc.union(bs) }

    val beaconMap = firstBeacons
        .map { coord ->
            coord to
                    this.filter { it.first.beacons.contains(coord) }
                        .map { bd -> distMap[bd.second.distance]!!.second.beacons }
                        .fold(secondBeacons) { rest, bs -> rest.intersect(bs) }
        }
        .toMap()

    return beaconMap
}

fun allowableOrientation(delta1: Coordinate, delta2: Coordinate): Set<Orientation> =
    allOrientations
        .filter { delta1 == it.rotate(delta2) }
        .toSet()

// takes a map of absolute coordinates of beacons to relative coords for scanner and returns
// best estimate for location and rotation
fun Map<Coordinate, Set<Coordinate>>.findBestLocationAndOrientation(): Pair<Coordinate, Orientation>? {
    val locations = this
    if (this.size < 12)
        return null

    val seen = mutableMapOf<Orientation, Int>()
    val ref = locations.filter { (k, v) -> v.size == 1}.entries.first()
    locations.filter { it != ref }
        .flatMap { (k, v) -> v.map { k to it}}
        .flatMap { (k, v) -> allowableOrientation(k - ref.key, v - ref.value.first()) }
        .forEach { seen[it] = (seen[it]?:0) + 1 }

    val best = seen.maxByOrNull { (k, v) -> v }?.key
    return best?.let { orientation ->
        val location = ref.key - orientation.rotate(ref.value.first())
        Pair(location, orientation)
    }
}

fun List<Scanner>.determineAllScanners(): List<Scanner> {
    val todo = this.slice(1 until this.size).toMutableList()
    val open = mutableListOf<Scanner>(this[0])
    val determinedScanners = mutableListOf<Scanner>(this[0])
    while (open.isNotEmpty()) {
        val knownScanner = open.removeFirst()
        val freshScanners: List<Scanner> =
            todo
                .mapNotNull { todoScanner ->
                    val commonDistanceBeacons = knownScanner
                        .toDistances()
                        .map { BeaconDistance(it.beacons.map { knownScanner.relToAbs(it) }.toSet(), it.distance) }
                        .intersection(todoScanner.toDistances())
                    commonDistanceBeacons
                        .overlappingBeaconsInDistList()
                        .findBestLocationAndOrientation()?.let { (loc, rot) ->
                            todoScanner.location = loc
                            todoScanner.orientation = rot
                            todoScanner
                        }
                }
        freshScanners.forEach { scanner ->
            open.add(scanner)
            determinedScanners.add(scanner)
            todo.remove(scanner)
        }
    }

    return determinedScanners
}


// returns all beacons in absolute coordinates
fun List<Scanner>.findAllBeacons(): Set<Coordinate> {
    val beacons = this
        .determineAllScanners()
        .flatMap { scanner -> scanner.beaconLocations() }
        .toSet()

    return beacons
}

fun List<Scanner>.findLargestDifference():Int {
    val scanners = this.determineAllScanners()

    return (0 until scanners.size-1).flatMap{ i1 ->
        (i1+1 until scanners.size).map { i2 ->
            scanners[i1].location?.manhattan(scanners[i2].location!!)?:0
        }
    }.maxOrNull()?:0

}

fun main() {
    val challenge = Day19()
    val solutions = mapOf(
        "part1" to challenge.part1(),
        "part2" to challenge.part2(),
    )
    println("Solutions: ")
    println(Gson().toJson(solutions))
}