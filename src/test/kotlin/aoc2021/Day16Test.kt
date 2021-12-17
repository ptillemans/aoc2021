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
        challenge.input="8A004A801A8002F478"
        val actual = challenge.part1()
        val expected = "16"
        assertEquals(expected, actual)
    }
   
    @Test
    fun testPart2() {
        challenge.input = "9C0141080250320F1802104A08"
        val actual = challenge.part2()
        val expected = "1"
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
        val expected = Pair(LiteralPacket(version = 6, payload = 2021), "000")
        val actual = binary.binaryParsePacket()
        assertEquals(expected, actual)
    }

    @Test
    fun testParseOperatorPacket() {
        val binary ="00111000000000000110111101000101001010010001001000000000"
        val expected = LessThanPacket(version = 1,  payload = listOf(
                LiteralPacket(6, 10),
                LiteralPacket(2, 20),
            ))
        val actual = binary.binaryParsePacket().first
        assertEquals(expected, actual)
    }

    @Test
    fun testParseOperator1Packet() {
        val binary ="11101110000000001101010000001100100000100011000001100000"
        val expected = MaximumPacket(version = 7,  payload = listOf(
                LiteralPacket(2, 1),
                LiteralPacket(4, 2),
                LiteralPacket(1, 3),
            ))
        val actual = binary.binaryParsePacket().first
        assertEquals(expected, actual)
    }

    @Test
    fun testSumOfVersions() {
        assertEquals(16, "8A004A801A8002F478".parsePacket().sumOfVersions())
        assertEquals(12, "620080001611562C8802118E34".parsePacket().sumOfVersions())
        assertEquals(23, "C0015000016115A2E0802F182340".parsePacket().sumOfVersions())
    }

    @Test
    fun testEval() {
        assertEquals(3, "C200B40A82".parsePacket().eval())
        assertEquals(54,"04005AC33890".parsePacket().eval())
        assertEquals(7, "880086C3E88112".parsePacket().eval())
        assertEquals(9, "CE00C43D881120".parsePacket().eval())
        assertEquals(1, "D8005AC2A8F0".parsePacket().eval())
        assertEquals(0, "F600BC2D8F".parsePacket().eval())
        assertEquals(0, "9C005AC2F8F0".parsePacket().eval())
        assertEquals(1, "9C0141080250320F1802104A08".parsePacket().eval())
    }

}
