package aoc2021
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

class Day16Test {
    private val input = """
""".trimIndent()
    
    private var challenge = Day16() 
    
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
    fun testHexToDec() {
        val hex = "D2FE28"
        val expected = "110100101111111000101000"
        val actual = hex.hexToBin()
        assertEquals(expected, actual)
    }

    @Test
    fun testParseLiteralPacket() {
        val binary ="110100101111111000101000"
        val expected = Pair(LiteralPacket(version = 6, payload = listOf(7, 14, 5)), "000")
        val actual = binary.parsePacket()
        assertEquals(expected, actual)
    }

    @Test
    fun testParseOperatorPacket() {
        val binary ="00111000000000000110111101000101001010010001001000000000"
        val expected = OperatorPacket(version = 1,  payload = listOf(
            LiteralPacket(6, listOf(10)),
            LiteralPacket(2, listOf(1, 4)),
        ))
        val actual = binary.parsePacket() as OperatorPacket
        assertEquals(expected, actual)
    }

}
