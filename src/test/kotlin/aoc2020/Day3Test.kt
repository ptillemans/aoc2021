package aoc2020
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

class Day3Test {
    private val input = """
""".trimIndent()
    
    private var challenge = Day3() 
    
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
    
}
