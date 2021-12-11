package aoc2021
import common.IntMatrix
import common.toIntMatrix
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

class Day11Test {

    private val smallExample = readExample("/aoc2021/day11/small.txt")
    private val largeExample = readExample("/aoc2021/day11/sample.txt")

    // load all matrices from the example in a map iteration -> matrix
    private fun readExample(filename:String):Map<Int, IntMatrix> =
        Day11Test::class.java.getResource(filename)!!.readText()
        .split("\n\n")
        .associate {
            val lines = it.split("\n")
            val iteration = if (lines[0].startsWith("After step "))
                lines[0].removePrefix("After step ").removeSuffix(":").toInt()
            else
                0
            iteration to lines.drop(1).joinToString("\n").toIntMatrix()
        }

    private var challenge = Day11() 

    @Test
    fun `test increase energy from small example`() {
        val actual = smallExample[1]!!.increaseEnergy()
        assertEquals(smallExample[2], actual)
    }

    @Test
    fun `test small example with flash`() {
        val actual = smallExample[0]!!.increaseEnergy().handleFlashes()
        assertEquals(smallExample[1], actual.first)
        assertEquals(9, actual.second)
    }

    @Test
    fun `test flash of octopi at with no octopi already flashed`() {
        val actual = IntMatrix(3, listOf( 0, 0, 0, 0, 9, 0, 0, 0, 0)).flash(Pair(1,1), setOf())
        val expected = IntMatrix(3, listOf( 1, 1, 1, 1, 0, 1, 1, 1, 1))
        assertEquals(expected, actual)
    }

    @Test
    fun `test flash with an octopus which has already flashed this turn`() {
        val actual = IntMatrix(3, listOf( 0, 0, 0, 0, 9, 0, 0, 0, 0)).flash(Pair(1,1), setOf(Pair(0,0)))
        val expected = IntMatrix(3, listOf( 0, 1, 1, 1, 0, 1, 1, 1, 1))
        assertEquals(expected, actual)
    }

    @Test
    fun `test all cases of the large worked example`() {
        var result = largeExample[0]!!
        var totalFlashes = 0
        (1..100).forEach {
            val (newResult, flashes) = result.increaseEnergy().handleFlashes()
            totalFlashes += flashes
            result = newResult
            if (largeExample[it] != null) {
                assertEquals(largeExample[it], result)
            }
        }
    }

    @Test
    fun `test count iterations till all octopi flash`() {
        val initial = largeExample[0]!!
        assertEquals(195, initial.cyclesTillAllFlash() )
    }

    @Test
    fun `test Part1`() {
        challenge.input = largeExample[0]!!
        val actual = challenge.part1()
        val expected = "1656"
        assertEquals(expected, actual)
    }
   
    @Test
    fun `test Part2`() {
        challenge.input = largeExample[0]!!
        val actual = challenge.part2()
        val expected = "195"
        assertEquals(expected, actual)
    }
    
}
