package aoc2021
import common.splitOn
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

class Day4Test {
    private val input = """
        7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1

        22 13 17 11  0
         8  2 23  4 24
        21  9 14 16  7
         6 10  3 18  5
         1 12 20 15 19

         3 15  0  2 22
         9 18 13 17  5
        19  8  7 25 23
        20 11 10 24  4
        14 21 16 12  6

        14 21 17 24  4
        10 16 15  9 19
        18  8 23 26 20
        22 11 13  6  5
         2  0 12  3  7
        """.trimIndent().split("\n")
    
    private var challenge = Day4()

    @BeforeEach
    fun setUp() {
        challenge.input = input
    }

    private val randomNumbers =
        listOf(7, 4, 9, 5, 11, 17, 23, 2, 0, 14, 21, 24, 10, 16, 13, 6, 15, 25, 12, 22, 18, 20, 8, 19, 3, 26, 1)

    val board1 = listOf(
        listOf(22, 13, 17, 11, 0),
        listOf(8, 2, 23, 4, 24),
        listOf(21, 9, 14, 16, 7),
        listOf(6, 10, 3, 18, 5),
        listOf(1, 12, 20, 15, 19),
    )
    val board2 = listOf(
        listOf(3, 15,  0,  2, 22),
        listOf(9, 18, 13, 17,  5),
        listOf(19,  8,  7, 25, 23),
        listOf(20, 11, 10, 24,  4),
        listOf(14, 21, 16, 12,  6),
    )
    val board3 = listOf(
        listOf(14, 21, 17, 24,  4),
        listOf(10, 16, 15,  9, 19),
        listOf(18,  8, 23, 26, 20),
        listOf(22, 11, 13,  6,  5),
        listOf(2,  0, 12,  3,  7),
    )
    val boards = listOf(board1, board2, board3)

    @Test
    fun parseInput() {
        val expected = Pair(randomNumbers, listOf(board1, board2, board3))
        val actual = challenge.parseInput(input)
        assertEquals(expected, actual)
    }

    @Test
    fun testBoardHasWonFalse() {
        val actual = (0 until 11)
            .map {randomNumbers.slice(0..it)}
            .flatMap { balls ->
                boards.filter {board -> board.hasWon(balls)}
            }

        assertTrue(actual.isEmpty())
    }

    @Test
    fun testBoardHasWonWithRow() {
        val balls = randomNumbers.slice(0 until 12)
        val actual = balls.indices
            .map {randomNumbers.slice(0..it)}
            .flatMap { bs ->
                boards.filter {board -> board.hasWon(bs)}
            }
            .first()

        assertEquals(board3, actual)
    }

    @Test
    fun testBoardHasWonWithColumn() {
        val balls = board1.map {it[0]}
        assertTrue(board1.hasWon(balls))
    }

    @Test
    fun testBoardScore() {
        val balls = randomNumbers.slice(0 until 12)
        val actual = board3.score(balls)
        assertEquals(4512, actual)
    }
    @Test
    fun testPart1() {
        val actual = challenge.part1()
        val expected = "4512"
        assertEquals(expected, actual)
    }
   
    @Test
    fun testPart2() {
        val actual = challenge.part2()
        val expected = "1924"
        assertEquals(expected, actual)
    }
    
}

