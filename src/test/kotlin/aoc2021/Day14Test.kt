package aoc2021
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

class Day14Test {
    private val text = Day14Test::class.java.getResource("/aoc2021/day14/sample.txt")!!.readText()

    private val test_steps = listOf(
         "NNCB",
         "NCNBCHB",
         "NBCCNBBBCBHCB",
         "NBBBCNCCNBBNBNBBCHBHHBCHB",
         "NBBNBNBBCCNBCNCCNBBNBBNBBBNBBNBBCBHCBHHNHCBBCBHCB",
    )
    private var challenge = Day14()
    
    @Test
    fun testParseInput() {
        val input = text.parseInputDay14()
        assertEquals(test_steps[0], input.template)
        assertEquals(16, input.rules.size)
    }

    @Test
    fun testBimerPolymerization() {
        val input = text.parseInputDay14()
        val rule = input.rules.first()
        val expected = listOf( Pair(rule.bimer.first, rule.addition ), Pair(rule.addition, rule.bimer.second))
        assertEquals(expected, input.rules.bimerPolymerization(rule.bimer))
    }

    @Test
    fun testElementCounts() {
        val input = text.parseInputDay14()
        val actual = input.reaction(10).countElements(input.template)
        assertEquals(1749, actual['B'])
        assertEquals(298, actual['C'])
        assertEquals(865, actual['N'])
        assertEquals(161, actual['H'])
    }

    @Test
    fun testPart1() {
        challenge.input = text.parseInputDay14()
        val actual = challenge.part1()
        val expected = "1588"
        assertEquals(expected, actual)
    }

    @Test
    fun testChainPolymerization2() {
        val input = text.parseInputDay14()
        (0 until test_steps.size-1)
            .forEach {
                assertEquals(test_steps[it+1].toBimerMap(),
                    input.rules.chainPolymerization(test_steps[it].toBimerMap()))
            }
    }

    @Test
    fun testPolymerizationReaction2() {
        val input = text.parseInputDay14()
        assertEquals(3073, input.reaction(10).map { it.value}.sum() + 1)
    }


    @Test
    fun testReactionPart2() {
        val input = text.parseInputDay14()
        val actual = input.reaction(40).countElements(input.template)
        assertEquals(2192039569602, actual['B'])
        assertEquals( 3849876073, actual['H'])
    }

    @Test
    fun testPart2() {
        challenge.input = text.parseInputDay14()
        val actual = challenge.part2()
        val expected = "2188189693529"
        assertEquals(expected, actual)
    }
    
}
