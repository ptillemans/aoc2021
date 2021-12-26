package aoc2021
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import kotlin.streams.asStream
import kotlin.streams.toList

class Day21Test {
    private val input = Pair(4,8)
    
    private var challenge = Day21() 
    
    @BeforeEach
    fun setUp() {
        challenge.input = input
    }
   
    @Test
    fun testPart1() {
        val actual = challenge.part1()
        val expected = "739785"
        assertEquals(expected, actual)
    }
   
    @Test
    fun testPart2() {
        val actual = challenge.part2()
        val expected = "444356092776315"
        assertEquals(expected, actual)
    }
    
    @Test
    fun testDeterministicDie() {
        val die = DeterministicDie()
        val result = (1..1000)
            .map {Pair(it, die.throwDie())}

        assertTrue(result.all { (it.first - 1) % 100 + 1 == it.second })

    }

    @Test
    fun testPlayer1_1() {
        val die = DeterministicDie()
        val player1 = Player(4)
        val player2 = Player(8)
        assertFalse(player1.play(die))
        assertEquals(10, player1.pos)
        assertFalse(player2.play(die))
        assertEquals(3, player2.pos)
        assertFalse(player1.play(die))
        assertEquals(4, player1.pos)
        assertFalse(player2.play(die))
        assertEquals(6, player2.pos)
        assertFalse(player1.play(die))
        assertEquals(6, player1.pos)
        assertFalse(player2.play(die))
        assertEquals(7, player2.pos)
        assertFalse(player1.play(die))
        assertEquals(6, player1.pos)
        assertFalse(player2.play(die))
        assertEquals(6, player2.pos)
    }

    @Test
    fun testPlayGame() {
        val (p1, p2) = playGame(4, 8);

        assertTrue(p1.hasWon())
        assertFalse(p2.hasWon())
    }

    @Test
    fun testDirac() {
        val (n1, n2) = playDirac(4,8)
        assertEquals(444356092776315, n1)
        assertEquals(341960390180808, n2)
    }

}

