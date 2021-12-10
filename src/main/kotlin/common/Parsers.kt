package common


fun String.toIntList(): List<Int> =
    this.split(",")
        .map { it.trim().toInt() }

