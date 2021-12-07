package aoc2021
import common.toIntList
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

class Day7Test {
    private val input = """16,1,2,0,4,2,7,1,2,14""".trimIndent().toIntList()
    
    private var challenge = Day7() 
    
    @BeforeEach
    fun setUp() {
        challenge.input = input
    }

    @Test
    fun testFuelConsuption() {
        assertEquals(37, input.fuelConsumption(2) )
        assertEquals(41, input.fuelConsumption(1) )
        assertEquals(39, input.fuelConsumption(3) )
    }

    @Test
    fun findOptimum() {
        val (actualX, actualConsumption) = input.findOptimum(fuelConsumption)
        assertEquals(2, actualX)
        assertEquals(37, actualConsumption)
    }
    @Test
    fun testPart1() {
        val actual = challenge.part1()
        val expected = "37"
        assertEquals(expected, actual)
    }

    @Test
    fun testFuelConsuption2() {
        assertEquals(168, input.fuelConsumption2(5) )
        assertEquals(206, input.fuelConsumption2(2) )
    }

    @Test
    fun testPart2() {
        val actual = challenge.part2()
        val expected = "168"
        assertEquals(expected, actual)
    }
    
}
