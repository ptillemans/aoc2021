package aoc2021

import com.google.gson.Gson
import java.lang.System.lineSeparator
import kotlin.system.measureNanoTime

typealias Element = Char
typealias Bimer = Pair<Element, Element>
data class Rule(val bimer: Bimer, val addition: Element)
data class Day14Input(val template: String, val rules: List<Rule>)

class Day14 {
    
    var input: Day14Input
    
    init {
        val filename = "/aoc2021/day14/input.txt"
        input = Day14::class.java.getResource(filename)!!.readText().parseInputDay14()
    }
    

    fun part1(): String {
        val counts = input.reaction(10)
            .countElements(input.template)
            .map { it.value }
            .sorted()
        val result = counts.last() - counts.first()
        return result.toString()
    }

   
    fun part2(): String {
        val counts = input.reaction(40)
            .countElements(input.template)
            .map { it.value }
            .sorted()
        val result = counts.last() - counts.first()
        return result.toString()
    }
    
}

fun String.parseInputDay14(): Day14Input {
    val parts = this.split(lineSeparator() + lineSeparator())
    val template = parts[0]
    val rules = parts[1].split(lineSeparator())
        .filter {isNotEmpty()}
        .map { it.trim().split(" -> ").zipWithNext().first() }
        .map { line ->
            Rule(line.first.split("")
                    .filter {it.isNotEmpty()}
                    .map { it[0] }
                    .zipWithNext()
                    .first(),
                line.second[0])
        }
    return Day14Input(template, rules)
}

fun List<Rule>.bimerPolymerization(bimer: Bimer): List<Bimer> =
    this.filter { it.bimer == bimer }
        .flatMap {listOf(Bimer(it.bimer.first, it.addition), Bimer(it.addition, it.bimer.second))}

fun List<Rule>.chainPolymerization(polymer: Map<Bimer, Long>): Map<Bimer, Long> =
    polymer
        .flatMap { bimer -> this.bimerPolymerization(bimer.key).map { it to bimer.value}}
        .fold(mapOf<Bimer,Long>()) {acc, x -> acc + Pair(x.first, x.second + (acc[x.first]?:0)) }

fun Day14Input.reaction(rounds: Int) : Map<Bimer, Long> =
    (0 until rounds).fold(this.template.toBimerMap()) { polymer, _ ->
        this.rules.chainPolymerization(polymer)
    }

fun String.toBimerMap(): Map<Bimer, Long> =
    this.split("")
        .filter {it.isNotEmpty()}
        .map{ it[0] }
        .zipWithNext()
        .groupBy { it }
        .map { it.key to it.value.size.toLong() }
        .toMap()

fun Map<Bimer,Long>.countElements(template: String): Map<Element, Long> {
    val endElements = setOf(template.first(), template.last())
    return this.flatMap { (k, v) -> k.toList().map { it to v } }
        .fold<Pair<Element, Long>, Map<Element, Long>>(mapOf()) { acc, p ->
            acc + Pair(p.first, (acc[p.first] ?: 0) + p.second)
        }
            // beginning and end elements are only once counted
        .map { it.key to if (it.key in endElements) it.value + 1 else it.value }
        .associate { it.first to it.second / 2 }    // we counted all elements double
}

fun main() {
    val challenge = Day14()
    val solutions: Map<String, String>
    val time = measureNanoTime {
        solutions = mapOf (
            "part1" to challenge.part1(),
            "part2" to challenge.part2(),
        )
    }
    println("Solutions: ")
    println(Gson().toJson(solutions))
    println("running in " + (time.toFloat()/1.0e6).toString() + "ms" )
}