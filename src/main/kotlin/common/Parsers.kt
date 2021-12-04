package common


fun String.toIntList(): List<Int> =
    this.split(",")
        .map { it.toInt() }

fun List<String>.toIntMatrix() : List<List<Int>> =
    this.map { line -> line
        .split(" +".toRegex())
        .filter { it.isNotEmpty() }
        .map { it.toInt() }
    }
