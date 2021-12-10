package common

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class IntMatrixTest {


    private val input = """
        2199943210
        3987894921
        9856789892
        8767896789
        9899965678""".trimIndent()

    private val matrix:  IntMatrix
        get() {
            return input.toIntMatrix(Int.MAX_VALUE)
        }
    private val input1 = """
        22 13 17 11  0
         8  2 23  4 24
        21  9 14 16  7
         6 10  3 18  5
         1 12 20 15 19
        """.trimIndent().split("\n")

    val matrix1 = IntMatrix(5 , listOf(
        22, 13, 17, 11, 0,
        8, 2, 23, 4, 24,
        21, 9, 14, 16, 7,
        6, 10, 3, 18, 5,
        1, 12, 20, 15, 19,
    ))

    @Test
    fun testToMatrix() {
        val actual = input1.toIntMatrix()
        Assertions.assertEquals(matrix1, actual)
    }

    @Test
    fun parseInputDimensions() {
        assertEquals(5, matrix.nRows)
        assertEquals(10, matrix.nColumns)
    }

    @Test
    fun parseInputBody() {
        assertEquals(2, matrix.element(0, 0))
        assertEquals(5, matrix.element(2, 2))
        assertEquals(8, matrix.element(9, 4))
    }

    @Test
    fun testMatrixBoundaries() {
        assertEquals(Int.MAX_VALUE, matrix.element(-1, 0))
        assertEquals(Int.MAX_VALUE, matrix.element(matrix.nColumns, 0))
        assertEquals(Int.MAX_VALUE, matrix.element(0, -1))
        assertEquals(Int.MAX_VALUE, matrix.element(0, matrix.nColumns))
    }

    @Test
    fun testMatrixRow() {
        val line = input.split("\n")[1].split("").filter { it.isNotBlank() }
        val expected = line.map { it.toInt() }
        assertEquals(expected, matrix.row(1))
    }

    @Test
    fun testMatrixColumn() {
        val lines = input.split("\n")
        val expected = lines.map { it[2].toString().toInt() }
        assertEquals(expected, matrix.column(2))
    }

    @Test
    fun testRows() {
        assertEquals((0 until matrix.nRows).map{matrix.row(it)}, matrix.rows())
    }

    @Test
    fun testColumns() {
        assertEquals((0 until matrix.nColumns).map{matrix.column(it)}, matrix.columns())
    }
}
