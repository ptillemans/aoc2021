package aoc2021

import com.google.gson.Gson
import common.splitOn
import java.lang.IllegalStateException
import java.lang.System.lineSeparator

class Day24 {

    var input: String

    init {
        val filename = "/aoc2021/day24/input.txt"
        input = Day24::class.java.getResource(filename).readText()
    }


    fun part1(): String? {
        return null
    }

    fun part2(): String? {
        return null
    }

    data class SectionIO(val zOut:Long, val zIn: Long, val wIn: Long)

    fun findValidNumbers(): List<Long>? {
        val prg = input.parseProgram()
        val digitParts = prg.splitOn { it.opcode == Opcode.Inp }.filter { it.isNotEmpty() }

        val preamble = listOf(
            Instruction(Opcode.Inp, registerMap["z"]!!, Implicit),
            Instruction(Opcode.Inp, registerMap["w"]!!, Implicit)
        )
        var zRange: Set<Long> = setOf(0L)

        var wDigits: MutableList<List<SectionIO>> = mutableListOf()
        for (prgPart in digitParts.reversed()) {
            val prg = preamble + prgPart
            val combos: List<SectionIO> = (1L..9).flatMap { w ->
                (-260L..260).map { z ->
                    Pair(z, w)
                }
            }
                .map { prg.execute(it.toList())?.registers?.get("z")!! to it }
                .filter { it.first in zRange }
                .map { SectionIO(it.first, it.second.first, it.second.second) }
                .toList()

            wDigits.add(combos)
            zRange = combos.map { it.zIn }.toSet()
        }




        return wDigits.findBestNumbers(0)
    }
}

fun List<List<Day24.SectionIO>>.findBestNumbers(zOut: Long) : List<Long>?{

    if (this.isEmpty()) {
        println("found a solution!!!!!")
        return  listOf()
    }

    val sectionMap = this.first()
        .filter { it.zOut == zOut }
        .groupBy { it.wIn }
    val rest = this.drop(1)
    println( "${this.size}")

    val sortedBy = sectionMap.entries.sortedBy { -it.key }
    for ((digit, sections) in sortedBy) {
        var bestNumbers = null;
        while  {
            bestNumbers = rest.findBestNumbers()
        }
    }
    val result : List<Long>? = sortedBy
        .flatMap { (digit, sections) ->
            sections
                .map { section -> rest.findBestNumbers(section.zIn) }
                .filterNotNull()
                .map { it + digit }
        }.firstOrNull()

    for ()

        return result
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

sealed class MonadSource() {}
data class MonadRegister(val name:String, val value:Long = 0) : MonadSource(){}
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

val initialState = MonadState( registerMap.keys.map { it to 0L}.toMap(), listOf())

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

