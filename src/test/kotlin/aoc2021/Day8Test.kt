package aoc2021

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

class Day8Test {
    private val input = """
        be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe
        edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc
        fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg
        fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb
        aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea
        fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb
        dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe
        bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef
        egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb
        gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce""".trimIndent()

    private val sampleLine = "acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | " +
            "cdfeb fcadb cdfeb cdbaf"

    private val outputResults = mapOf(

        "fdgacbe cefdb cefbgd gcbe" to 8394,
        "fcgedb cgb dgebacf gc" to 9781,
        "cg cg fdcagb cbg" to 1197,
        "efabcd cedba gadfec cb" to 9361,
        "gecf egdcabf bgf bfgea" to 4873,
        "gebdcfa ecba ca fadegcb" to 8418,
        "cefg dcbef fcge gbcadfe" to 4548,
        "ed bcgafe cdgba cbgef" to 1625,
        "gbdfcae bgc cg cgb" to 8717,
        "fgae cfgab fg bagce" to 4315,
        )

    private var challenge = Day8()

    @BeforeEach
    fun setUp() {
        challenge.input = input
    }

    @Test
    fun ParseLine() {
        val data = "acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | " +
                "cdfeb fcadb cdfeb cdbaf"

        val (digits, output) = challenge.parseLine(data)
        assertEquals(10, digits.size)
        assertEquals(4, output.size)
    }

    @Test
    fun testParseInput() {
        val actual = challenge.parseInput(input)
        assertEquals(10, actual.size)
    }

    @Test
    fun testDigitsAreSorted() {
        val data = challenge.parseInput(input)
        for (s in data) {
            for (digit in (s.first + s.second)) {
                val expected = digit.toCharArray().sorted().joinToString("")
                assertEquals(expected, digit)
            }
        }
    }

    @Test
    fun testDigitMapping() {
        val data = challenge.parseLine(sampleLine)
        val mapping = challenge.mapDigits(data.first)
        assertEquals(0, mapping["abcdeg"])
        assertEquals(1, mapping["ab"])
        assertEquals(2, mapping["acdfg"])
        assertEquals(3, mapping["abcdf"])
        assertEquals(4, mapping["abef"])
        assertEquals(5, mapping["bcdef"])
        assertEquals(6, mapping["bcdefg"])
        assertEquals(7, mapping["abd"])
        assertEquals(8, mapping["abcdefg"])
        assertEquals(9, mapping["abcdef"])
    }

    @Test
    fun testLineOutput() {
        val data = challenge.parseLine(sampleLine)
        val output = challenge.lineOutput(data)
        assertEquals(5353, output)
    }

    @Test
    fun testFindDigit2() {
        val data = challenge.parseLine(sampleLine)
        val digit2 = challenge.findDigit2(data.first)
        assertEquals("acdfg", digit2)
    }

    @Test
    fun uniqueOutputPatterns() {
        val data = challenge.parseInput(input)
        val actual = challenge.uniqueOutputPatterns(data)
        assertEquals(26, actual)
    }

    @Test
    fun testPart1() {
        val actual = challenge.part1()
        val expected = "26"
        assertEquals(expected, actual)
    }

    @Test
    fun testPart2() {
        val actual = challenge.part2()
        val expected = "61229"
        assertEquals(expected, actual)
    }

}
