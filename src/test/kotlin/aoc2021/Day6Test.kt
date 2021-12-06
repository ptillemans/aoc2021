package aoc2021
import common.toIntList
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import java.math.BigInteger

class Day6Test {
    private val input = """
        Initial state: 3,4,3,1,2
        After  1 day:  2,3,2,0,1
        After  2 days: 1,2,1,6,0,8
        After  3 days: 0,1,0,5,6,7,8
        After  4 days: 6,0,6,4,5,6,7,8,8
        After  5 days: 5,6,5,3,4,5,6,7,7,8
        After  6 days: 4,5,4,2,3,4,5,6,6,7
        After  7 days: 3,4,3,1,2,3,4,5,5,6
        After  8 days: 2,3,2,0,1,2,3,4,4,5
        After  9 days: 1,2,1,6,0,1,2,3,3,4,8
        After 10 days: 0,1,0,5,6,0,1,2,2,3,7,8
        After 11 days: 6,0,6,4,5,6,0,1,1,2,6,7,8,8,8
        After 12 days: 5,6,5,3,4,5,6,0,0,1,5,6,7,7,7,8,8
        After 13 days: 4,5,4,2,3,4,5,6,6,0,4,5,6,6,6,7,7,8,8
        After 14 days: 3,4,3,1,2,3,4,5,5,6,3,4,5,5,5,6,6,7,7,8
        After 15 days: 2,3,2,0,1,2,3,4,4,5,2,3,4,4,4,5,5,6,6,7
        After 16 days: 1,2,1,6,0,1,2,3,3,4,1,2,3,3,3,4,4,5,5,6,8
        After 17 days: 0,1,0,5,6,0,1,2,2,3,0,1,2,2,2,3,3,4,4,5,7,8
        After 18 days: 6,0,6,4,5,6,0,1,1,2,6,0,1,1,1,2,2,3,3,4,6,7,8,8,8,8""".trimIndent()
        .split("\n")
        .map {
            it.substringAfter(":").trim()
        }
        .map { it.toIntList() }


    private var challenge = Day6() 
    
    @BeforeEach
    fun setUp() {
        challenge.input = input[0]
    }

    @Test
    fun testToPopulationFrequency() {
        val expected = mapOf(
            0 to BigInteger.valueOf(0),
            1 to BigInteger.valueOf(1),
            2 to BigInteger.valueOf(1),
            3 to BigInteger.valueOf(2),
            4 to BigInteger.valueOf(1),
            5 to BigInteger.valueOf(0),
            6 to BigInteger.valueOf(0),
            7 to BigInteger.valueOf(0),
            8 to BigInteger.valueOf(0),
        )
        val actual = input[0].toPopulationFrequency()
        assertEquals(expected, actual)
    }


    @Test
    fun testUpateTimer() {
        val actual = input[0].toPopulationFrequency().updateTimers()
        val expected = input[1].toPopulationFrequency()
        assertEquals(expected,actual)
    }

    @Test
    fun testUpateTimerReset() {
        val data = input[1].toPopulationFrequency()
        val actual = data.updateTimers()
        val expected = input[2].toPopulationFrequency().toMutableMap()
        expected.remove(8)
        assertEquals(expected[6],actual[6])
    }

    @Test
    fun testNewBorns() {
        val expected = BigInteger.valueOf(1)
        val actual = input[1].toPopulationFrequency().newBorns()
        assertEquals(expected,actual)

    }

    @Test
    fun testNewBorns2() {
        val data = input[3].toPopulationFrequency()
        val actual = data.newBorns()
        assertEquals(BigInteger.valueOf(2),actual)

    }

    @Test
    fun testNewGenerations() {
        assertTrue(input.zipWithNext()
            .map { Pair(it.first.toPopulationFrequency().newGeneration(), it.second.toPopulationFrequency()) }
            .all{ it.first == it.second })
    }

    @Test
    fun testPart1() {
        val actual = challenge.part1()
        val expected = "5934"
        assertEquals(expected, actual)
    }

    @Test
    fun testPart2() {
        val actual = challenge.part2()
        val expected = "26984457539"
        assertEquals(expected, actual)
    }
    
}
