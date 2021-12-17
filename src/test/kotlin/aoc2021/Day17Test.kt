package aoc2021
import common.Point
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

class Day17Test {
    private val input = """target area: x=20..30, y=-10..-5"""
    
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
    fun testPart1() {
        val actual = challenge.part1()
        val expected = null
        assertEquals(expected, actual)
    }
   
    @Test
    fun testPart2() {
        val actual = challenge.part2()
        val expected = null
        assertEquals(expected, actual)
    }
    
}
