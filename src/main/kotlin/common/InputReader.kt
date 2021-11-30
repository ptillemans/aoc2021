package common

class InputReader {
}

fun readInput(year: Int, day: Int) : String {
    val filename = "aoc${year}/day${day}/input.txt"
    return InputReader::class.java.getResource(filename).readText()
}