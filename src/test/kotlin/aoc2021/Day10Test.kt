package aoc2021

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import java.math.BigInteger

class Day10Test {
    private val input = """
        [({(<(())[]>[[{[]{<()<>>
        [(()[<>])]({[<{<<[]>>(
        {([(<{}[<>[]}>{[]{[(<()>
        (((({<>}<{<{<>}{[]{[]{}
        [[<[([]))<([[{}[[()]]]
        [{[{({}]{}}([{[{{{}}([]
        {<[[]]>}<{[{[{[]{()[[[]
        [<(<(<(<{}))><([]([]()
        <{([([[(<>()){}]>(<<{{
        <{([{{}}[<[[[<>{}]]]>[]]
""".trimIndent()

    private var challenge = Day10()

    @BeforeEach
    fun setUp() {
        challenge.input = input.split("\n").filter { it.isNotEmpty() }
    }

    @Test
    fun testNavSyntaxValid() {
        assertEquals(NavParseResult.Valid, "()".checkSyntax())
        assertEquals(NavParseResult.Valid, "[]".checkSyntax())
    }

    @Test
    fun testNavSyntaxCorrupted() {
        assertEquals(NavParseResult.Corrupt(']'), "(]".checkSyntax())
        assertEquals(NavParseResult.Corrupt('>'), "{()()()>".checkSyntax())
        assertEquals(NavParseResult.Corrupt('}'), "(((()))}".checkSyntax())
        assertEquals(NavParseResult.Corrupt(')'), "<([]){()}[{}])".checkSyntax())
    }

    @Test
    fun testNavSyntaxIncomplete() {
        val cases = mapOf(
            "[({(<(())[]>[[{[]{<()<>>" to "}}]])})]",
            "[(()[<>])]({[<{<<[]>>(" to ")}>]})",

            "(((({<>}<{<{<>}{[]{[]{}" to "}}>}>))))",

            "{<[[]]>}<{[{[{[]{()[[[]" to "]]}}]}]}>",

            "<{([{{}}[<[[[<>{}]]]>[]]" to "])}>",
        )
        for ((k, v) in cases) {
            assertEquals(k.checkSyntax(), NavParseResult.Incomplete(v))
        }


    }

    @Test
    fun testScore() {
        val cases = mapOf(
            "}}]])})]" to 288957,
            ")}>]})" to 5566,
            "}}>}>))))" to 1480781,
            "]]}}]}]}>" to 995444,
            "])}>" to 294,
        )
        for ((k,v) in cases) {
            assertEquals(BigInteger.valueOf(v.toLong()), k.scoreMissing())
        }
    }

    @Test
    fun testPart1() {
        val actual = challenge.part1()
        val expected = "26397"
        assertEquals(expected, actual)
    }

    @Test
    fun testPart2() {
        val actual = challenge.part2()
        val expected = "288957"
        assertEquals(expected, actual)
    }

}
