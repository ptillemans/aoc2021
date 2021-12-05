package aoc2021
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import kotlin.math.acos

class Day3Test {
    private val input = """
        00100
        11110
        10110
        10111
        10101
        01111
        00111
        11100
        10000
        11001
        00010
        01010 
        """.trimIndent()
    
    private var challenge = Day3() 
    
    @BeforeEach
    fun setUp() {
        challenge.input = input
    }
   
    @Test
    fun testPart1() {
        val actual = challenge.part1()
        val expected = "198"
        assertEquals(expected, actual)
    }
   
    @Test
    fun testPart2() {
        val actual = challenge.part2()
        val expected = "230"
        assertEquals(expected, actual)
    }

    @Test
    fun testToVector() {
        val actual = "10010".toVector()
        val expected = listOf(1, 0, 0, 1, 0)
        assertEquals(expected, actual)
    }

    @Test
    fun testParseInput() {
        val actual = challenge.parseInput(input)
        assertEquals(12, actual.size)
    }

    @Test
    fun testGamma() {
        val data = challenge.parseInput(input)
        val gamma = data.calculateGamma()
        assertEquals(22, gamma)
    }

    @Test
    fun testEpsilon() {
        val data = challenge.parseInput(input)
        val epsilon = data.calculateEpsilon()
        assertEquals(9, epsilon)
    }

    @Test
    fun testAddVector() {
        val a = listOf(1, 2, 3, 4, 5)
        val b = listOf(2,1,2,1,2)
        val actual = a.addVector(b)
        val expected = listOf(3,3,5,5,7)
        assertEquals(expected, actual)
    }

    @Test
    fun testScrubBits() {
        val data = challenge.parseInput(input)
        val pred = {x:Int -> x >= data.size/2}
        val actual = data.scrubBits(0, pred)
        val expected = listOf(
            listOf(1,1,1,1,0),
            listOf(1,0,1,1,0),
            listOf(1,0,1,1,1),
            listOf(1,0,1,0,1),
            listOf(1,1,1,0,0),
            listOf(1,0,0,0,0),
            listOf(1,1,0,0,1),
        )
        assertEquals(expected, actual)
        val actual2 = actual.scrubBits(1, pred)
        val expected2 = listOf(
            listOf(1,0,1,1,0),
            listOf(1,0,1,1,1),
            listOf(1,0,1,0,1),
            listOf(1,0,0,0,0),
        )
        assertEquals(expected2, actual2)
    }

    @Test
    fun testOxygenGenerationRating() {
        val data = challenge.parseInput(input)
        val actual = data.oxygenGeneratorRating()
        assertEquals(23, actual)
    }

    @Test
    fun testCo2ScrubberRating() {
        val data = challenge.parseInput(input)
        val actual = data.co2ScrubberRating()
        assertEquals(10, actual)
    }

}
