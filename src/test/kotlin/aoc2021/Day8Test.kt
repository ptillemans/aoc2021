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
        val expected = null
        assertEquals(expected, actual)
    }
    
}
