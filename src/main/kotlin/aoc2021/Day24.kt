package aoc2021

import com.google.gson.Gson
import common.splitOn
import java.lang.IllegalStateException
import java.lang.System.lineSeparator

class Day24 {

    var input: String

    init {
        val filename = "/aoc2021/day24/input.txt"
        input = Day24::class.java.getResource(filename)!!.readText()
    }


    fun part1(): String = solveSerialNumber(false).toString()


    fun part2(): String = solveSerialNumber(true).toString()

    private val preamble = listOf(
        Instruction(Opcode.Inp, registerMap["z"]!!, Implicit),
        Instruction(Opcode.Inp, registerMap["w"]!!, Implicit)
    )

    private val programSections = input
        .parseProgram()
        .splitOn { it.opcode == Opcode.Inp }
        .filter { it.isNotEmpty() }
        .map { preamble + it }


    private fun executeSection(n: Int, w: Long, z: Long) : Long =
        programSections[n].execute(listOf(z, w))?.registers?.get("z")!!

    fun findValidNumbers(n: Int, zOut: Long): List<Long>? {

        if (n<0) {
            return listOf()
        }

        for (w in (1L..9).reversed()) {
            for (z in -5000L..5000) {
                if (executeSection(n, w, z) == zOut) {
                    val rest = findValidNumbers(n - 1, z)
                    if (rest != null) {
                        return listOf(w) + rest
                    }
                }
            }
        }
        return  null
    }

    private fun solveSerialNumber(part2: Boolean): Any {
        val blocks = input.split(lineSeparator()).filter{it.isNotBlank()}.chunked(18)
        val result = MutableList(14) { -1 }
        val buffer = ArrayDeque<Pair<Int, Int>>()
        fun List<String>.lastOf(command: String) = last { it.startsWith(command) }.split(" ").last().toInt()
        val best = if (part2) 1 else 9
        blocks.forEachIndexed { index, instructions ->
            if ("div z 26" in instructions) {
                val offset = instructions.lastOf("add x")
                val (lastIndex, lastOffset) = buffer.removeFirst()
                val difference = offset + lastOffset
                if (difference >= 0) {
                    result[lastIndex] = if (part2) best else best - difference
                    result[index] = if (part2) best + difference else best
                } else {
                    result[lastIndex] = if (part2) best - difference else best
                    result[index] = if (part2) best else best + difference
                }
            } else buffer.addFirst(index to instructions.lastOf("add y"))
        }

        return result.joinToString("").toLong()
    }
}

fun main() {
    val challenge = Day24()
    val solutions = mapOf (
        "part1" to challenge.part1(),
        "part2" to challenge.part2(),
    )
    println("Solutions: ")
    println(Gson().toJson(solutions))
}

enum class Opcode {
    Inp,
    Add,
    Mul,
    Div,
    Mod,
    Eql,
}

sealed class MonadSource
data class MonadRegister(val name:String, val value:Long = 0) : MonadSource()
data class MonadLiteral(
    val n: Long,
    val value: Long = n
) : MonadSource()
object Implicit: MonadSource()

val registerMap = mapOf(
    "w" to MonadRegister("w"),
    "x" to MonadRegister("x"),
    "y" to MonadRegister("y"),
    "z" to MonadRegister("z"),
)

data class Instruction(val opcode: Opcode, val target: MonadRegister, val source: MonadSource)

fun String.capitalize() : String =
    this.toCharArray()
        .let{cs -> cs[0].titlecase() + cs.slice(1 until cs.size)
            .joinToString("")
        }

fun String.parseCommand(): Instruction {
    val parts = this.split(" ")
    val opcode = Opcode.valueOf(parts[0].capitalize())
    val target = registerMap[parts[1]]!!
    val source =
        if (parts.size < 3)
            Implicit
        else if (parts[2][0].let {it.isDigit() || it == '-'})
            MonadLiteral(parts[2].toLong())
        else
            registerMap[parts[2]]!!
    return Instruction(opcode, target, source)
}

fun String.parseProgram(): List<Instruction> =
    this.split(lineSeparator())
        .filter { it.isNotBlank() }
        .map {it.parseCommand()}

data class MonadState(val registers: Map<String, Long>, val inputs: List<Long>) {
    fun withInputs(inputs: List<Long>) : MonadState = MonadState(registers, inputs)
    fun withRegister(name: String, f: (Long) -> Long) : MonadState =
        MonadState(
            registers + (name to f(registers[name]!!)),
            inputs
        )

}

val initialState = MonadState(registerMap.keys.associateWith { 0L }, listOf())

fun List<Instruction>.execute(inputs: List<Long>) : MonadState? =
    this.fold(initialState.withInputs(inputs)) { state: MonadState?, instruction ->
        state?.let{ instruction.execute(it) } }

fun Instruction.execute(state: MonadState): MonadState? =
    when (this.opcode) {
        Opcode.Inp -> MonadState(
            state.registers + (this.target.name to state.inputs.first()),
            state.inputs.drop(1)
        )
        Opcode.Add -> state.withRegister(this.target.name) { it + state.evalSource(this.source) }
        Opcode.Mul -> state.withRegister(this.target.name) { it * state.evalSource(this.source) }
        Opcode.Div -> if (state.evalSource(this.source) != 0L)
            state.withRegister(this.target.name) { it / state.evalSource(this.source) }
        else
            null
        Opcode.Mod -> if (state.evalSource(this.source) > 0L || state.registers[this.target.name]!! < 0L)
            state.withRegister(this.target.name) { it % state.evalSource(this.source) }
        else
            null
        Opcode.Eql -> state.withRegister(this.target.name) { if(it == state.evalSource(this.source)) 1 else 0}

    }

fun MonadState.evalSource(src: MonadSource): Long =
    when(src) {
        is MonadLiteral -> src.value
        is MonadRegister -> this.registers[src.name]!!
        is Implicit -> throw IllegalStateException()
    }

