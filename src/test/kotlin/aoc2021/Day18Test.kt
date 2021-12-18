package aoc2021
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import java.lang.System.lineSeparator

class Day18Test {
    private val input = Day18Test::class.java.getResource("/aoc2021/day18/sample.txt")!!.readText()

    private var challenge = Day18() 
    
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
    fun testParser() {
        input.split(lineSeparator())
            .map {it.toSnailFishNumber()}
            .forEach { assertTrue(it is SnailFishNumber) }
    }

    @Test
    fun testParser2() {
        val actual = "[[9,8], 1]".toSnailFishNumber()
        val expected = SnailFishNumber(
            SnailFishNumber(
                RegularNumber(9),
                RegularNumber(8)
            ),
            RegularNumber(1)
        )
        assertEquals(expected, actual)
    }

    @Test
    fun testReduce1() {
        val actual = "[[[[[9,8],1],2],3],4]".toSnailFishNumber().reduce()
        val expected = "[[[[0,9],2],3],4]".toSnailFishNumber()
        assertEquals(expected, actual)
    }

    @Test
    fun testReduce2() {
        val actual = "[7,[6,[5,[4,[3,2]]]]]".toSnailFishNumber().reduce()
        val expected = "[7,[6,[5,[7,0]]]]".toSnailFishNumber()
        assertEquals(expected, actual)
    }


    @Test
    fun testReduce3() {
        val actual = "[[6,[5,[4,[3,2]]]],1]".toSnailFishNumber().reduce()
        val expected = "[[6,[5,[7,0]]],3]".toSnailFishNumber()
        assertEquals(expected, actual)
    }

    @Test
    fun testExplode() {
        val actual = "[[9,8], 1]".toSnailFishNumber().explode()
        val expected = "[0,9]".toSnailFishNumber()
        assertEquals(expected, actual)
    }

    @Test
    fun testSplit() {
        val actual = RegularNumber(11).split()
        val expected = SnailFishNumber(RegularNumber(5),RegularNumber(6))
        assertEquals(expected, actual)
    }

    fun testAddition() {
        val a = SnailFishNumber(1.toSnailFishElement(),2.toSnailFishElement())
        val b = SnailFishNumber(Pair(3,4).toSnailFishElement(),5.toSnailFishElement())
        val expected = SnailFishNumber(a, b)
        assertEquals(expected, a + b)
    }
}
