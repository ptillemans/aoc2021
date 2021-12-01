package aoc2020
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

class Day1Test {
    private val input = """
        """.trimIndent()
    
    private var challenge = Day1() 
    
    @BeforeEach
    fun setUp() {
        challenge.input = input
    }
   
    @Test
    fun testPart1() {
        val actual = challenge.part1()
        assertEquals(null, actual)
    }
   
    @Test
    fun testPart2() {
        val actual = challenge.part2()
        assertEquals(null, actual)
    }
    
}
