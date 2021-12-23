package aoc2021
import common.Point
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import java.lang.System.lineSeparator

class Day20Test {
    private val input = Day20Test::class.java.getResource("/aoc2021/day20/sample.txt").readText()
    private val enhance1 = Day20Test::class.java.getResource("/aoc2021/day20/enhance1.txt").readText()
    private val enhance2 = Day20Test::class.java.getResource("/aoc2021/day20/enhance2.txt").readText()

    private var challenge = Day20()
    
    @BeforeEach
    fun setUp() {
        challenge.input = input
    }
   
    @Test
    fun testPart1() {
        val actual = challenge.part1()
        val expected = "35"
        assertEquals(expected, actual)
    }
   
    @Test
    fun testPart2() {
        val actual = challenge.part2()
        val expected = "3351"
        assertEquals(expected, actual)
    }


    @Test
    fun testParseInput() {
        val (eia, img) = input.parseDay20()
        val expectedEia = input.split(lineSeparator()).first().count { it == '#' }
        val expectedImage = input.split(lineSeparator()).drop(2).joinToString("").count { it == '#' }
        assertEquals(expectedEia, eia.count { it })
        assertEquals(expectedImage, img.image.size)
    }

    @Test
    fun testEnhanceImage() {
        val (iea, img) = input.parseDay20()
        val expected  = enhance1.parseImage(offset = Point(-1, -1))
        val enhancedImage = img.enhance(iea)
        assertEquals(expected, enhancedImage)
    }

    @Test
    fun testEnhance2Image() {
        val (iea, img) = input.parseDay20()
        val expected  = enhance2.parseImage(offset = Point(-2, -2))
        val enhancedImage = img.enhance(iea).enhance(iea)
        assertEquals(expected, enhancedImage)
    }

    @Test
    fun testPixelCode() {
        val (iea, img) = input.parseDay20()
        val expected = 34
        val actual = img.pixelCode(Point(1,1), 0)
    }
}
