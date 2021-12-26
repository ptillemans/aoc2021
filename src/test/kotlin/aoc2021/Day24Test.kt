package aoc2021
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import java.lang.System.`in`
import java.lang.System.lineSeparator

class Day24Test {
    private var challenge = Day24()
    
    @Test
    fun testPart1() {
        val actual = challenge.part1()
        val expected = null
        assertEquals(expected, actual)
    }
   
    @Test
    fun testPart2() {
        val actual = challenge.part2()
        val expected = null
        assertEquals(expected, actual)
    }

    @Test
    fun testCapitalise() {
        assertEquals("Abcd", "abcd".capitalize())
    }

   @Test
   fun testParseInp() {
       val prg = "inp w".parseCommand()

       assertEquals(Opcode.Inp, prg.opcode)
       assertEquals(registerMap["w"], prg.target)
       assertEquals(Implicit, prg.source)
   }

    @Test
    fun testParseAdd() {
        val prg = "add z w".parseCommand()

        assertEquals(Opcode.Add, prg.opcode)
        assertEquals(registerMap["z"], prg.target)
        assertEquals(registerMap["w"], prg.source)
    }

    @Test
    fun testParseMul() {
        val prg = "mul z 25".parseCommand()

        assertEquals(Opcode.Mul, prg.opcode)
        assertEquals(registerMap["z"], prg.target)
        assertEquals(MonadLiteral(25), prg.source)
    }

    @Test
    fun testParseDiv() {
        val prg = "div z w".parseCommand()

        assertEquals(Opcode.Div, prg.opcode)
        assertEquals(registerMap["z"], prg.target)
        assertEquals(registerMap["w"], prg.source)
    }

    @Test
    fun testParseMod() {
        val prg = "mod z w".parseCommand()

        assertEquals(Opcode.Mod, prg.opcode)
        assertEquals(registerMap["z"], prg.target)
        assertEquals(registerMap["w"], prg.source)
    }

    @Test
    fun testParseEql() {
        val prg = "eql x 0".parseCommand()

        assertEquals(Opcode.Eql, prg.opcode)
        assertEquals(registerMap["x"], prg.target)
        assertEquals(MonadLiteral(0), prg.source)
    }

    @Test
    fun testRunProgram() {
        val prg = """
            inp x
            mul x -1
            """.trimIndent()
            .replace("\n", lineSeparator())
            .parseProgram()
        val state = prg.execute(listOf(6))
        assertEquals(-6, state?.registers?.get("x") ?: 0 )

    }

    @Test
    fun testRunProgram2() {
        val prg = """
            inp z
            inp x
            mul z 3
            eql z x
            """.trimIndent()
            .replace("\n", lineSeparator())
            .parseProgram()
        val state = prg.execute(listOf(1, 3))
        assertEquals(1, state?.registers?.get("z") ?: 0 )
        val state2 = prg.execute(listOf(1, 4))
        assertEquals(0, state2?.registers?.get("z") ?: 0 )
    }

    @Test
    fun testRunProgram3() {
        val prg = """
            inp w
            add z w
            mod z 2
            div w 2
            add y w
            mod y 2
            div w 2
            add x w
            mod x 2
            div w 2
            mod w 2
            """.trimIndent()
            .replace("\n", lineSeparator())
            .parseProgram()
        var state = prg.execute(listOf(15))
        assertEquals(1, state?.registers?.get("w") ?: 0 )
        assertEquals(1, state?.registers?.get("x") ?: 0 )
        assertEquals(1, state?.registers?.get("y") ?: 0 )
        assertEquals(1, state?.registers?.get("z") ?: 0 )
        state = prg.execute(listOf(0))
        assertEquals(0, state?.registers?.get("w") ?: 0 )
        assertEquals(0, state?.registers?.get("x") ?: 0 )
        assertEquals(0, state?.registers?.get("y") ?: 0 )
        assertEquals(0, state?.registers?.get("z") ?: 0 )
        state = prg.execute(listOf(10))
        assertEquals(1, state?.registers?.get("w") ?: 0 )
        assertEquals(0, state?.registers?.get("x") ?: 0 )
        assertEquals(1, state?.registers?.get("y") ?: 0 )
        assertEquals(0, state?.registers?.get("z") ?: 0 )
    }

    @Test
    fun testSerialNumber() {
        val inputs = "13579246899999".split("")
            .filter {it.isNotEmpty()}
            .map { it.toLong()}
        val prg = challenge.input.parseProgram()

        val state = prg.execute(inputs)

        assertNotNull(state)
    }

    @Test
    fun testFindSerial() {
        val actual = challenge.findValidNumbers()
        assertEquals(14, actual)
    }
}
