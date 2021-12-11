package aoc2021
import common.IntMatrix
import common.toIntMatrix
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

class Day11Test {
    private val smallExample = """
        Before any steps:
        11111
        19991
        19191
        19991
        11111

        After step 1:
        34543
        40004
        50005
        40004
        34543

        After step 2:
        45654
        51115
        61116
        51115
        45654
        """.trimIndent()
        .split("\n\n")
        .map { it.split("\n").drop(1).joinToString("\n") }
        .map { it.toIntMatrix()}

    val largeExample = Day11Test::class.java.getResource("/aoc2021/day11/sample.txt").readText()
        .split("\n\n")
        .associate {
            val lines = it.split("\n")
            val iteration = if (lines[0].startsWith("After step "))
                lines[0].removePrefix("After step ").removeSuffix(":").toInt()
            else
                0
            iteration to lines.drop(1).joinToString("\n").toIntMatrix()
        }

    private var challenge = Day11() 


    @BeforeEach
    fun setUp() {
    }

    @Test
    fun testIncreaseEnergy() {
        val actual = smallExample[1].increaseEnergy()
        assertEquals(smallExample[2], actual)
    }

    @Test
    fun testHandleFlashes() {
        val actual = smallExample[0].increaseEnergy().handleFlashes()
        assertEquals(smallExample[1], actual.first)
        assertEquals(9, actual.second)
    }

    @Test
    fun testFlash() {
        val actual = IntMatrix(3, listOf( 0, 0, 0, 0, 9, 0, 0, 0, 0)).flash(Pair(1,1), setOf())
        val expected = IntMatrix(3, listOf( 1, 1, 1, 1, 0, 1, 1, 1, 1))
        assertEquals(expected, actual)
    }

    @Test
    fun testFlashWithHasFlashed() {
        val actual = IntMatrix(3, listOf( 0, 0, 0, 0, 9, 0, 0, 0, 0)).flash(Pair(1,1), setOf(Pair(0,0)))
        val expected = IntMatrix(3, listOf( 0, 1, 1, 1, 0, 1, 1, 1, 1))
        assertEquals(expected, actual)
    }

    @Test
    fun testLargeExample() {
        var result = largeExample[0]!!
        var totalFlashes = 0
        (1..100).forEach {
            val (newResult, flashes) = result.increaseEnergy().handleFlashes()
            totalFlashes += flashes
            result = newResult
            if (largeExample[it] != null) {
                assertEquals(largeExample[it], result)
                println("Iteration ${it} verified.")
            }
            println("Iteration ${it} with ${totalFlashes}.")
        }
    }

    @Test
    fun testIterateTillAllFlash() {
        val initial = largeExample[0]!!
        assertEquals(195, initial.cyclesTillAllFlash() )
    }

    @Test
    fun testPart1() {
        challenge.input = largeExample[0]!!
        val actual = challenge.part1()
        val expected = "1656"
        assertEquals(expected, actual)
    }
   
    @Test
    fun testPart2() {
        challenge.input = largeExample[0]!!
        val actual = challenge.part2()
        val expected = "195"
        assertEquals(expected, actual)
    }
    
}
