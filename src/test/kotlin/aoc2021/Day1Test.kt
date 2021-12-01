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
        val actual = challenge.countIncreases(data)
        assertEquals(7, actual)
    }

    @Test
    fun testPart1() {
        val actual = challenge.part1()
        val expected = "8"
        assertEquals(expected, actual)
    }

    @Test
    fun testGrouping() {
        val groups = challenge.groupData(3, challenge.parseInput(input))
        assertEquals(groups[0], listOf(199, 200, 208))
        assertEquals(groups.size, 7)
    }

    @Test
    fun testPart2() {
        val actual = challenge.part2()
        val expected = "5"
        assertEquals(expected, actual)
    }
    
}
