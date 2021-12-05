package aoc2021
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

class Day5Test {
    private val input = """
        0,9 -> 5,9
        8,0 -> 0,8
        9,4 -> 3,4
        2,2 -> 2,1
        7,0 -> 7,4
        6,4 -> 2,0
        0,9 -> 2,9
        3,4 -> 1,4
        0,0 -> 8,8
        5,5 -> 8,2
        """.trimIndent()
    
    private var challenge = Day5() 
    
    @BeforeEach
    fun setUp() {
        challenge.input = input
    }

    val lines = listOf(
        Line(Point(0,9), Point(5,9)),
        Line(Point(8,0), Point(0,8)),
        Line(Point(9,4), Point(3,4)),
        Line(Point(2,2), Point(2,1)),
        Line(Point(7,0), Point(7,4)),
        Line(Point(6,4), Point(2,0)),
        Line(Point(0,9), Point(2,9)),
        Line(Point(3,4), Point(1,4)),
        Line(Point(0,0), Point(8,8)),
        Line(Point(5,5), Point(8,2)),
        )

    @Test
    fun testParseInput() {
        val actual = challenge.parseInput(input)
        assertEquals(lines, actual)
    }

    @Test
    fun testCoveredPoints1() {
        val line = "1,1 -> 1,3".toLine()
        val expected = setOf(
            "1,1".toPoint(),
            "1,2".toPoint(),
            "1,3".toPoint()
        )
        val actual = line.coveredPoints()
        assertEquals(expected, actual)
    }

    @Test
    fun testCoveredPoints2() {
        val line = "9,7 -> 7,7".toLine()
        val expected = setOf(
            "9,7".toPoint(),
            "8,7".toPoint(),
            "7,7".toPoint()
        )
        val actual = line.coveredPoints()
        assertEquals(expected, actual)
    }

    @Test
    fun testCoveredMap() {
        var coverMap: CoverMap = mapOf()
        val line = "9,7 -> 7,7".toLine()
        val expected = setOf(
            "9,7".toPoint(),
            "8,7".toPoint(),
            "7,7".toPoint()
        )
        coverMap = coverMap.addLine(line)
        for (p in expected) {
            assertEquals(1, coverMap[p])
        }
        assertEquals(3, coverMap.size)
    }

    @Test
    fun testCoveredMapOverlapAreas() {
        val expected = setOf(
            "3,4".toPoint(),
            "7,4".toPoint(),
            "0,9".toPoint(),
            "1,9".toPoint(),
            "2,9".toPoint(),
        )
        val coverMap: CoverMap = lines.fold(mapOf()) { acc, line -> acc.addLine(line) }

        for (p in expected) {
            assertEquals(2, coverMap[p])
        }
    }

    @Test
    fun testCoveredMapOverlapAreasWithDiagonals() {
        val coverMap: CoverMap = lines.fold(mapOf()) { acc, line -> acc.addLine(line, true) }

        val actual = coverMap.values.count { it >= 2 }
        assertEquals(12, actual)
    }

    @Test
    fun testPart1() {
        val actual = challenge.part1()
        val expected = "5"
        assertEquals(expected, actual)
    }
   
    @Test
    fun testPart2() {
        val actual = challenge.part2()
        val expected = "12"
        assertEquals(expected, actual)
    }
    
}

