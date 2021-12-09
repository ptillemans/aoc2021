package aoc2021

import common.toIntMatrix
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*


class Day9Test {
    private val input = """
        2199943210
        3987894921
        9856789892
        8767896789
        9899965678""".trimIndent()

    private val heightMap: HeightMap
        get() {
            return input.toIntMatrix(9)
        }

    private var challenge = Day9()

    @BeforeEach
    fun setUp() {
        challenge.input = input
    }

    @Test
    fun testLocalMinima() {
        val expected = setOf(
            Pair(1, 0),
            Pair(9, 0),
            Pair(2, 2),
            Pair(6, 4)
        )
        assertEquals(expected, heightMap.localMinima())
    }

    @Test
    fun testFindBasin() {
        val expected = setOf(
            Pair(0, 0),
            Pair(0, 1),
            Pair(1, 0),
        )
        assertEquals(expected, heightMap.findBasin(Pair(1, 0)))
    }

    @Test
    fun testFindBasinsDecreasingSizes() {
        val expected = listOf(14, 9,9,3)
        assertEquals(expected, heightMap.findBasinsDecreasingSizes())
    }

    @Test
    fun testRiskLevel() {
        assertEquals(15, heightMap.riskLevel())
    }

    @Test
    fun testPart1() {
        val actual = challenge.part1()
        val expected = "15"
        assertEquals(expected, actual)
    }

    @Test
    fun testPart2() {
        val actual = challenge.part2()
        val expected = "1134"
        assertEquals(expected, actual)
    }

}

