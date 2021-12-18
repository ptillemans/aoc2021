package aoc2021

import com.google.gson.Gson
import io.ktor.utils.io.charsets.*
import jdk.jshell.Snippet
import kotlin.time.measureTime

sealed class SnailFishElement {}

data class SnailFishNumber(val left: SnailFishElement, val right: SnailFishElement) : SnailFishElement()
data class RegularNumber(val x: Int) : SnailFishElement()

fun Int.toSnailFishElement() = RegularNumber(this)
fun Pair<Int, Int>.toSnailFishElement() =
    SnailFishNumber(
        this.first.toSnailFishElement(),
        this.second.toSnailFishElement()
    )


fun String.toSnailFishNumber() : SnailFishNumber {
    var pos: Int = 0
    val text = this.replace(" *".toRegex(), "")

    fun parseSnailFishNumber(): SnailFishNumber {
        fun readNumber(): SnailFishElement =
            if (text.get(pos) == '[') {
                parseSnailFishNumber()
            } else {
                var number = 0
                while (text.get(pos).isDigit()) {
                    number = 10 * number + text.get(pos).toString().toInt()
                    pos += 1
                }
                number.toSnailFishElement()
            }

        pos += 1  // skip [
        val left = readNumber()
        pos += 1 //skip ,
        val right = readNumber()
        pos += 1 //skip ]
        return SnailFishNumber(left, right)
    }

    return parseSnailFishNumber()
}

operator fun SnailFishNumber.plus(b: SnailFishNumber) : SnailFishNumber =
    SnailFishNumber(this, b)

fun SnailFishNumber.reduce() : SnailFishNumber {
    fun checkSplits(element: SnailFishElement, level:Int): SnailFishElement =
        when(element) {
            is RegularNumber -> element
            is SnailFishNumber -> if (level < 4)
                SnailFishNumber(
                    checkSplits(element.left, level+1)!!,
                    checkSplits(element.right, level+1)!!
                )
            else
                if (element.left is SnailFishNumber || element.right is SnailFishNumber)
                    element.explode()!!
                else
                    element
        }

    return SnailFishNumber(
        checkSplits(this.left, 2),
        checkSplits(this.right,2)
    )
}

fun SnailFishNumber.explode(): SnailFishNumber? =
    when(this.left) {
        is SnailFishNumber ->
            when(this.right) {
                is SnailFishNumber -> null
                is RegularNumber -> Pair(0, this.right.x + (this.left.right as RegularNumber).x).toSnailFishElement()
            }
        is RegularNumber ->
            when(this.right) {
                is SnailFishNumber -> Pair(this.left.x + (this.right.left as RegularNumber).x, 0).toSnailFishElement()
                is RegularNumber -> null
            }
    }

fun RegularNumber.split(): SnailFishElement =
    if (this.x >= 10)
        SnailFishNumber(
            RegularNumber(this.x/2),
            RegularNumber(this.x - this.x/2)
        )
    else
        this

class Day18 {
    
    var input: String
    
    init {
        val filename = "/aoc2021/day18/input.txt"
        input = Day18::class.java.getResource(filename).readText()
    }
    

    fun part1(): String? {
        return null
    }
   
    fun part2(): String? {
        return null
    }
    
}

fun main() {
    val challenge = Day18()
    val solutions = mapOf (
        "part1" to challenge.part1(),
        "part2" to challenge.part2(),
    )
    println("Solutions: ")
    println(Gson().toJson(solutions))
}