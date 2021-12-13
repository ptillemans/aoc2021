package aoc2021
import common.readInput
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import java.lang.System.lineSeparator

class Day13Test {
    val inputText = Day11Test::class.java.getResource("/aoc2021/day13/sample.txt")!!.readText()
    val input = inputText.parseInputDay13()
    private var challenge = Day13()

    @Test
    fun testParser() {
        assertEquals(18, input.points.size)
        assertEquals(2, input.folds.size)
    }


    @Test
    fun `test folds`() {
        val foldVertical = input.folds[0]
        val foldHorizontal = input.folds[1]
        val points = foldVertical.doFold(input.points)
        assertEquals(17, points.size)
        assertEquals(16, foldHorizontal.doFold(points).size)
    }

    @Test
    fun `test apply folds`() {
        val points = input.applyFolds()
        assertEquals(16, points.size)
    }


    @Test
    fun testPart1() {
        challenge.input = input
        val actual = challenge.part1()
        val expected = "17"
        assertEquals(expected, actual)
    }

    @Test
    fun testFormatPoints() {
        val points = input.applyFolds()
        val actual = points.format()
        actual.split(lineSeparator())
            .forEach {
                assertTrue(it.matches("[0-9]+ [0-9]+".toRegex()))
            }
    }
    @Test
    fun testPart2() {
        challenge.input = input
        val actual = challenge.part2()
        val expected = input.applyFolds().format()
        assertEquals(expected, actual)
    }
    
}
