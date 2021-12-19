package aoc2021
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

class Day19Test {
    private val input = Day19Test::class.java.getResource("/aoc2021/day19/sample.txt").readText()
    
    private var challenge = Day19() 
    
    @BeforeEach
    fun setUp() {
        challenge.input = input
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

    @Test
    fun testParser() {
        val scanners = input.toScanners()
        assertEquals(5, scanners.size)
        assertEquals(Coordinate(0,0,0), scanners[0].location)
        assertEquals(Orientation(Rotation.Rot0, Rotation.Rot0, Rotation.Rot0), scanners[0].orientation)
    }

    @Test
    fun testIntersection() {
        val scanners = inpu
    }
}
