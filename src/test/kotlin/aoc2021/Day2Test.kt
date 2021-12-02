package aoc2021
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

class Day2Test {
    private val input = """
        forward 5
        down 5
        forward 8
        up 3
        down 8
        forward 2
        """.trimIndent()
    
    private var challenge = Day2() 
    
    @BeforeEach
    fun setUp() {
        challenge.input = input
    }

    @Test
    fun testParseInput()
    {
        val actual = challenge.parseInput(input)
        val expected = listOf<Command>(
            Command(Direction.FORWARD, 5),
            Command(Direction.DOWN, 5),
            Command(Direction.FORWARD, 8),
            Command(Direction.UP, 3),
            Command(Direction.DOWN, 8),
            Command(Direction.FORWARD, 2)
        )
        assertEquals(expected, actual)
    }

    @Test
    fun testCalculatePosition()
    {
        val actual = challenge.parseInput(input).calculateCartesianPosition()
        val expected = Position(15, 10)
        assertEquals(expected, actual)
    }

    @Test
    fun testPart1() {
        val actual = challenge.part1()
        val expected = "150"
        assertEquals(expected, actual)
    }

    @Test
    fun testCalculateAimedPosition()
    {
        val actual = challenge.parseInput(input).calculateAimedPosition()
        assertEquals(15, actual.x)
        assertEquals(60, actual.depth)
    }

    @Test
    fun testPart2() {
        val actual = challenge.part2()
        val expected = "900"
        assertEquals(expected, actual)
    }
    
}
