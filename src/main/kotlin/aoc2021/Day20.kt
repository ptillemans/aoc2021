package aoc2021

import com.google.gson.Gson
import common.Point
import java.lang.System.lineSeparator

typealias ImageEnhancementAlgorithm = Array<Boolean>
typealias Bounds = Pair<Point, Point>
data class Day20Image(val bounds: Bounds, val image: Set<Point>) {
    fun inBounds(pos: Point) =
        (pos.first in bounds.first.first..bounds.second.first)
                && (pos.second in bounds.first.second .. bounds.second.second)
}

class Day20 {

    var input: String


    init {
        val filename = "/aoc2021/day20/input.txt"
        input = Day20::class.java.getResource(filename).readText()
    }


    fun part1(): String =
        input.parseDay20().let { (eia, img) ->
            val image1 = img.enhance(eia, 0)
            val image2 = image1.enhance(eia, if(eia[0]) 1 else 0)
            println("${image1.image.size} - ${image2.image.size}")
            image2.image.count().toString()
        }

    fun part2(): String {
        val (eia, img) = input.parseDay20()
        var image = img
            (0 until 50).forEach {
                val default = if (eia[0]) it%2 else 0
                image = image.enhance(eia, default)
            }
        return image.image.count().toString()
    }



}

fun String.parseDay20(): Pair<ImageEnhancementAlgorithm, Day20Image> {
    val parts = this.split(lineSeparator() + lineSeparator())
    val eia = parts[0].map { c -> (c == '#') }
        .toTypedArray()
    val image = parts[1].parseImage()
    return Pair(eia, image)
}

fun String.parseImage(offset: Point = Point(0, 0)): Day20Image {
    val img = this
        .split(lineSeparator())
        .filter { isNotBlank() }
        .flatMapIndexed { row, line ->
            line
                .mapIndexed { col, ch ->
                    if (ch == '#') offset + Point(row, col) else null
                }
                .filterNotNull()
        }
        .toSet()
    val bounds = img.bounds()
    return Day20Image(bounds, img)
}

fun Day20Image.enhance(iae: ImageEnhancementAlgorithm, default: Int = 0): Day20Image =
    this.bounds.let { (min, max) ->

        val image = (min.first - 1..max.first + 1).flatMap { r ->
                (min.second - 3..max.second + 3).map { c ->
                Point(r, c)
            }
        }
            .filter { pos ->
                val pc = this.pixelCode(pos, default)
                iae[pc]
            }
            .toSet()
        val bounds = image.bounds()
        Day20Image(bounds, image)
    }

fun Set<Point>.bounds(): Pair<Point, Point> {
    val minR = this.minOf { it.first }
    val maxR = this.maxOf { it.first }

    val minC = this.minOf { it.second }
    val maxC = this.maxOf { it.second }

    return Pair(Point(minR, minC), Point(maxR, maxC))
}

fun Day20Image.pixelCode(pos: Point, default: Int) =
    (-1..1).flatMap { dr ->
        (-1..1).map { dc ->
            val p = pos + Point(dr, dc)
            if (this.inBounds(p))
                if (this.image.contains(pos + Point(dr, dc))) 1 else 0
            else
                default
        }
    }
        .reduce { acc, x -> acc * 2 + x }

fun main() {
    val challenge = Day20()
    val solutions = mapOf(
        "part1" to challenge.part1(),
        "part2" to challenge.part2(),
    )
    println("Solutions: ")
    println(Gson().toJson(solutions))
}