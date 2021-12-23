package aoc2021
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import kotlin.math.exp

class Day22Test {
    private val input = Day22Test::class.java.getResource("/aoc2021/day22/sample.txt").readText()
    
    private var challenge = Day22() 

    private val cubes = input.parseCubes()

    @BeforeEach
    fun setUp() {
        challenge.input = input
    }
   
    @Test
    fun testPart1() {
        challenge.input = Day22Test::class.java.getResource("/aoc2021/day22/sample2.txt").readText()
        val actual = challenge.part1()
        val expected = "590784"
        assertEquals(expected, actual)
    }
   
    @Test
    fun testPart2() {
        challenge.input = Day22Test::class.java.getResource("/aoc2021/day22/sample3.txt").readText()
        val actual = challenge.part2()
        val expected = "2758514936282235"
        assertEquals(expected, actual)
    }

    @Test
    fun testParseCubes() {
        assertEquals(4, cubes.size)
        assertEquals(cubes[0].second.x, Pair(10, 12))
        assertEquals(cubes[0].second.y, Pair(10, 12))
        assertEquals(cubes[0].second.z, Pair(10, 12))
        assertTrue(cubes[0].first)
        assertFalse(cubes[2].first)
    }

    @Test
    fun testVolume() {
        assertEquals(27, cubes[0].second.volume())
    }

    @Test
    fun testCombine() {
        val combined = cubes[0].second.remove(cubes[1].second)
        val expected = listOf(
            Cuboid(10 to 10, 10 to 12, 10 to 12),
            Cuboid( 11 to 12, 10 to 10,  10 to 12),
            Cuboid( 11 to 12, 11 to 12, 10 to 10),
        )
        assertEquals(expected, combined)
    }

    @Test
    fun testCombineMore() {
        val combined = cubes.slice(0..1).combine()
        val expected = listOf(
            Cuboid(10 to 10, 10 to 12, 10 to 12),
            Cuboid( 11 to 12, 10 to 10,  10 to 12),
            Cuboid( 11 to 12, 11 to 12, 10 to 10),
            Cuboid( 11 to 13, 11 to 13, 11 to 13)
        )
        assertEquals(expected, combined)
        assertEquals(27 + 19, combined.sumOf { it.volume() })
    }

    @Test
    fun testCombineMore2() {
        val combined = cubes.slice(0..2).combine()
        assertEquals(27 + 19 - 8, combined.sumOf { it.volume() })
    }

    @Test
    fun testCombineMore3() {
        val combined = cubes.combine()
        assertEquals(27 + 19 - 8 + 1, combined.sumOf { it.volume() })
    }

    @Test
    fun testIntersect() {
        val actual = cubes[0].second.intersect(cubes[2].second)
        val expected = Cuboid(Pair(10,11), Pair(10,11), Pair(10,11))
        assertEquals(expected, actual)
    }

    @Test
    fun testSample3() {

    }
}
