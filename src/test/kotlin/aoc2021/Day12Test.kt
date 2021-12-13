package aoc2021
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import java.lang.System.lineSeparator

data class Sample(
    val maze: String,
    val edges: List<Edge>,
    val nPathsPart1: Int,
    val pathsPart1: List<Path>? = null,
    val nPathsPart2: Int,
    val pathsPart2: List<Path>? = null
)

class Day12Test {

    private val sample = readExample("/aoc2021/day12/sample.txt")
    private val sample2 = readExample("/aoc2021/day12/sample2.txt")
    private val sample3 = readExample("/aoc2021/day12/sample3.txt")

    // load all matrices from the example in a map iteration -> matrix
    private fun readExample(filename:String):Sample {

        val text = Day11Test::class.java.getResource(filename)!!.readText()
        val parts = text
            .split(lineSeparator() + lineSeparator())

        val edges = parts[0].parseMaze()
        val nPathsPart1 = parts[1].toInt()
        val nPathsPart2 = if (parts[2].toCharArray()[0].isDigit())
            parts[2].toInt()
        else
            parts[3].toInt()
        val pathsPart1 = if (parts[2].isPathList())
            parts[2].parsePaths() else null

        val pathsPart2 = if (parts.size > 3 && parts[3].isPathList())
            parts[3].parsePaths()
        else if (parts.size > 4 && parts[4].isPathList())
            parts[4].parsePaths()
        else
            null

        return Sample(parts[0], edges, nPathsPart1, pathsPart1, nPathsPart2, pathsPart2)
    }

    private fun String.isPathList() = this.toCharArray()[0].isLetter()

    private fun String.parsePaths() =
        this.split(lineSeparator()).map {it.split(",")}


    private var challenge = Day12()

    @Test
    fun testAllPathsGenerated() {
        val actual = sample.edges.generatePaths()
        for (path in sample.pathsPart1!!) {
            assertTrue(actual.contains(path))
        }
    }

    @Test
    fun `test right amount of paths generated`() {
        val actual = sample.edges.generatePaths()
        assertEquals(sample.nPathsPart1, actual.size)
    }

    @Test
    fun `test slightly larger sample`() {
        val actual = sample2.edges.generatePaths()
        assertEquals(sample2.nPathsPart1, actual.size)
    }

    @Test
    fun `test much larger sample`() {
        val actual = sample3.edges.generatePaths()
        assertEquals(sample3.nPathsPart1, actual.size)
    }

    @Test
    fun testPart1() {
        challenge.input = sample3.maze
        val actual = challenge.part1()
        val expected = sample3.nPathsPart1.toString()
        assertEquals(expected, actual)
    }

    @Test
    fun `test part2 with small example`() {
        val actual = sample.edges.generatePaths(canVisit = canVisitPart2)
        assertEquals(sample.nPathsPart2, actual.size)

    }

    @Test
    fun `test part2 with larger example`() {
        val actual = sample2.edges.generatePaths(canVisit = canVisitPart2)
        assertEquals(sample2.nPathsPart2, actual.size)

    }

    @Test
    fun `test part2 with much larger example`() {
        val actual = sample3.edges.generatePaths(canVisit = canVisitPart2)
        assertEquals(sample3.nPathsPart2, actual.size)
    }

    @Test
    fun `test canVisitPart2 second visit small cave`() {
        val seen = mapOf( "c" to 1, "A" to 2)
        assertTrue(canVisitPart2(seen, "c"))
    }

    @Test
    fun `test canVisitPart2 third visit small cave`() {
        val seen = mapOf( "c" to 2, "A" to 2)
        assertFalse(canVisitPart2(seen, "c"))
    }

    @Test
    fun testPart2() {
        challenge.input = sample3.maze
        val actual = challenge.part2()
        val expected = sample3.nPathsPart2.toString()
        assertEquals(expected, actual)
    }
    
}
