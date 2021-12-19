package aoc2021
import common.Point
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import java.lang.System.lineSeparator

class Day17Test {
    private val input = """target area: x=20..30, y=-10..-5${lineSeparator()}""".trimMargin()

    private val samples = Day17Test::class.java.getResource("/aoc2021/day17/sample.txt")!!.readText()
        .split(lineSeparator())
        .flatMap { it.split(" +".toRegex()) }
        .flatMap {line -> line.split(",")
            .filter {it.isNotEmpty()}
            .map{it.toInt()}
            .zipWithNext()
        }
        .sorted()

    private var challenge = Day17()
    
    @BeforeEach
    fun setUp() {
        challenge.input = input
    }

    @Test()
    fun testParseTargetArea() {
        val targetArea = input.parseTargetArea()
        val expected = Pair(Point(20, -10), Point(30, -5))
        assertEquals(expected, targetArea)
    }

    @Test
    fun testMaxHeight() {
        val actual = input.parseTargetArea().maxHeight()
        assertEquals(45, actual)
    }

    @Test
    fun testGoodSpeeds() {
        val actual = input.parseTargetArea().goodSpeeds()
        val expected = samples.sorted()
        assertEquals(expected, actual)
    }
    @Test
    fun testPart1() {
        val actual = challenge.part1()
        val expected = "45"
        assertEquals(expected, actual)
    }
   
    @Test
    fun testPart2() {
        val actual = challenge.part2()
        val expected = "112"
        assertEquals(expected, actual)
    }
    
}
