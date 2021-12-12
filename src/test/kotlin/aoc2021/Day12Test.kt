package aoc2021
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import java.lang.System.lineSeparator

data class Sample(
    val maze: String,
    val edges: List<Edge>,
    val nPaths: Int,
    val paths: List<Path>? = null
)

class Day12Test {
    private val input = """
""".trimIndent()

    private val sample = readExample("/aoc2021/day12/sample.txt")
    private val sample2 = readExample("/aoc2021/day12/sample2.txt")
    private val sample3 = readExample("/aoc2021/day12/sample3.txt")

    // load all matrices from the example in a map iteration -> matrix
    private fun readExample(filename:String):Sample {

        val text = Day11Test::class.java.getResource(filename)!!.readText()
        val parts = text
            .split(lineSeparator() + lineSeparator())

        val edges = parts[0].parseMaze()
        val nPaths = parts[1].toInt()
        if (parts.size == 2) {
            return Sample(parts[0], edges, nPaths)
        }

        val paths = parts[2].split(lineSeparator())
            .map { it.split(",") }
        return Sample(parts[0], edges, nPaths, paths)
    }

    private var challenge = Day12()

    @Test
    fun testAllPathsGenerated() {
        val actual = sample.edges.generatePaths();
        for (path in sample.paths!!) {
            assertTrue(actual.contains(path))
        }
    }

    @Test
    fun `test right amount of paths generated`() {
        val actual = sample.edges.generatePaths();
        assertEquals(sample.nPaths, actual.size)
    }

    @Test
    fun `test slightly larger sample`() {
        val actual = sample2.edges.generatePaths();
        assertEquals(sample2.nPaths, actual.size)
    }

    @Test
    fun `test much larger sample`() {
        val actual = sample3.edges.generatePaths();
        assertEquals(sample3.nPaths, actual.size)
    }

    @Test
    fun testPart1() {
        challenge.input = sample3.maze
        val actual = challenge.part1()
        val expected = sample3.nPaths.toString()
        assertEquals(expected, actual)
    }
   
    @Test
    fun testPart2() {
        val actual = challenge.part2()
        val expected = null
        assertEquals(expected, actual)
    }
    
}
