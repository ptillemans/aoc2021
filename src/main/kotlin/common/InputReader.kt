package common

class InputReader {
}

fun readInput(year: Int, day: Int, filename: String = "input.txt") : String {
    val resourcename = "aoc${year}/day${day}/${filename}"
    return InputReader::class.java.getResource(resourcename).readText()
}