package common

typealias Point = Pair<Int, Int>

fun List<Int>.toPoint(): Point {
    require(this.size==2)
    val (a, b) = this
    return Point(a, b)
}

fun String.toPoint(): Point =
    this.split(',')
        .map {it.trim().toInt() }
        .toPoint()

operator fun Point.plus(b: Point) = Pair(this.first + b.first, this.second + b.second)
operator fun Point.minus(b: Point) = Pair(this.first - b.first, this.second - b.second)
