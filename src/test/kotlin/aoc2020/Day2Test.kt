package aoc2020

import aoc2021.Command
import aoc2021.Direction
import aoc2021.Direction.*
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
    fun testPart2() {
        val actual = challenge.part2()
        val expected = null
        assertEquals(expected, actual)
    }

}
