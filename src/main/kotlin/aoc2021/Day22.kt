package aoc2021

import com.google.gson.Gson
import common.toPoint
import java.lang.System.lineSeparator
import java.util.*

val ZeroPair = Pair(0, 0)

data class Cuboid(
    val x: Pair<Int,Int> = ZeroPair,
    val y: Pair<Int,Int> = ZeroPair,
    val z: Pair<Int,Int> = ZeroPair,
) {
    fun withX(nx: Pair<Int, Int>?) : Cuboid? = nx?.let {Cuboid(it, y, z)}
    fun withY(ny: Pair<Int, Int>?) : Cuboid? = ny?.let{Cuboid(x, it, z)}
    fun withZ(nz: Pair<Int, Int>?) : Cuboid? = nz?.let{Cuboid(x, y, it)}

    fun volume(): Long =
        span(x)*span(y)*span(z)

    private fun span(a :Pair<Int, Int>): Long = (a.second - a.first).toLong() + 1

    fun intersect(other: Cuboid) : Cuboid? =
        Cuboid()
            .withX(overlap(x, other.x))
            ?.withY(overlap(y, other.y))
            ?.withZ(overlap(z, other.z))


    fun remove(other: Cuboid) : List<Cuboid> {
        val overlap = this.intersect(other)
        if (overlap == null) {
            return listOf(this)
        }

        val result = mutableListOf<Cuboid>()
        var cube = this
        if (overlap.x.first > cube.x.first) {
            result.add(cube.withX(cube.x.first to overlap.x.first - 1)!!)
        }
        if (overlap.x.second < cube.x.second) {
            result.add(cube.withX(overlap.x.second+1 to cube.x.second)!!)
        }
        cube = cube.withX(overlap.x)!!
        if (overlap.y.first > cube.y.first) {
            result.add(cube.withY(cube.y.first to overlap.y.first - 1)!!)
        }
        if (overlap.y.second < cube.y.second) {
            result.add(cube.withY(overlap.y.second+1 to cube.y.second)!!)
        }

        cube = cube.withY(overlap.y)!!
        if (overlap.z.first > cube.z.first) {
            result.add(cube.withZ(cube.z.first to overlap.z.first - 1)!!)
        }
        if (overlap.z.second < cube.z.second) {
            result.add(cube.withZ(overlap.z.second+1 to cube.z.second)!!)
        }

        return result
    }

    private fun range(a:Pair<Int, Int>) = a.first..a.second

    private fun overlap(a: Pair<Int, Int>, b: Pair<Int, Int>) : Pair<Int, Int>? =
        if (b.first in range(a))
            if (b.second in range(a))
                b
            else
                Pair(b.first, a.second)
        else
            if (b.second in range(a))
                Pair(a.first, b.second)
            else
                if (a.first in range(b))
                    a
                else
                    null


    fun inPart1Area() : Boolean =
        x.first in -50..50 &&
                x.second in -50..50 &&
                y.first in -50..50 &&
                y.second in -50..50 &&
                z.first in -50..50 &&
                z.second in -50..50

}


class Day22 {
    
    var input: String
    
    init {
        val filename = "/aoc2021/day22/input.txt"
        input = Day22::class.java.getResource(filename).readText()
    }
    

    fun part1(): String =
        input.parseCubes()
            .filter {it.second.inPart1Area()}
            .combine()
            .sumOf { it.volume() }
            .toString()

   
    fun part2(): String =
        input.parseCubes()
            .combine()
            .sumOf { it.volume() }
            .toString()

}

fun String.parseCube(): Pair<Boolean, Cuboid> {
    val parts = this.split(" ")
    val on = parts[0] == "on"
    val c = parts[1].split(",")
        .map { it.split("=") }
        .map { range -> Pair(
            range[0],
            range[1]
                .split("..")
                .map{ it.toInt()}
                .toPoint()
        )}
        .fold(Cuboid()) { cube, r -> when (r.first) {
            "x" -> cube.withX(r.second)!!
            "y" -> cube.withY(r.second)!!
            "z" -> cube.withZ(r.second)!!
            else
                -> cube
        }}
    return Pair(on, c)
}

fun String.parseCubes() : List<Pair<Boolean,Cuboid>> =
    this.split(lineSeparator())
        .filter { it.isNotBlank() }
        .map { it.parseCube() }


fun List<Pair<Boolean,Cuboid>>.combine(): List<Cuboid> =
    this.fold(listOf()) {
            combined, (on, cube) -> if (on)
                combined.flatMap { it.remove(cube) } + cube
            else
                combined.flatMap { it.remove(cube) }
        }

fun main() {
    val challenge = Day22()
    val solutions = mapOf (
        "part1" to challenge.part1(),
        "part2" to challenge.part2(),
    )
    println("Solutions: ")
    println(Gson().toJson(solutions))
}