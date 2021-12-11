package common

data class IntMatrix(val nRows: Int, val body: List<Int>, val invalidValue: Int = Int.MAX_VALUE) {
    val nColumns: Int = body.size / nRows

    fun get(x: Int, y: Int): Int =
        if (x in 0 until nColumns && y in 0 until nRows)
            body[y * nColumns + x]
        else
            invalidValue

    fun set(x: Int, y: Int, v: Int): IntMatrix =
        if (x in 0 until nColumns && y in 0 until nRows)
            IntMatrix(nRows,
                body.slice(0 until y * nColumns + x )
                        + v
                        + body.slice(y * nColumns + x + 1 until nRows* nColumns))
        else
            IntMatrix(nRows, body)

    fun set(p: Pair<Int,Int>, v: Int): IntMatrix =
        this.set(p.first, p.second, v)

    fun elements(): List<Int> = body

    fun entries(): List<Pair<Pair<Int, Int>, Int>> =
        (0 until nRows).flatMap { y ->
            (0 until nColumns).map { x ->
                Pair(x, y)
            }
        }.zip(body)

    fun get(pos: Pair<Int, Int>): Int = pos.let { (x, y) -> get(x, y) }

    fun row(r: Int): List<Int> = body.slice(r * nColumns until (r + 1) * nColumns)

    fun column(c: Int): List<Int> = (c until c + nRows * nColumns step nColumns).map { body[it] }

    fun rows(): List<List<Int>> =
        (0 until nRows).map { row(it) }

    fun columns(): List<List<Int>> =
        (0 until nColumns).map { column(it) }

}

fun String.toIntMatrix(invalidValue: Int = Int.MAX_VALUE): IntMatrix {
    val lines = this.trim().split("\n")
    val nRows = lines.size
    val body = this.trim()
        .split("")
        .filter { it.isNotBlank() }
        .map { it.toInt() }
    return IntMatrix(nRows, body, invalidValue = invalidValue)
}

fun List<String>.toIntMatrix(invalidValue: Int = Int.MAX_VALUE): IntMatrix =
    IntMatrix(
        this.size,
        this.flatMap { line ->
            line
                .split(" +".toRegex())
                .filter { it.isNotEmpty() }
                .map { it.toInt() }
        },
        invalidValue = invalidValue
    )