package aoc2021
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

class Day1Test {
    private val input = """
        199
        200
        208
        210
        200
        207
        240
        269
        260
        263
        """.trimIndent()
    
    private var challenge = Day1() 
    
    @BeforeEach
    fun setUp() {
        challenge.input = input
    }

    @Test
    fun testCountIncreases() {
        val data = challenge.parseInput(input)
        val actual = data.countIncreases()
        assertEquals(7, actual)
    }

    @Test
    fun testPart1() {
        val actual = challenge.part1()
        val expected = "7"
        assertEquals(expected, actual)
    }

    @Test
    fun testPart2() {
        val actual = challenge.part2()
        val expected = "5"
        assertEquals(expected, actual)
    }
    
}
