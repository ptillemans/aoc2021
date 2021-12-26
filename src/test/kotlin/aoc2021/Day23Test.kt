package aoc2021
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import java.lang.System.`in`
import java.lang.System.lineSeparator

class Day23Test {
    private val input = """
        #############
        #...........#
        ###B#C#B#D###
          #A#D#C#A#
          #########""".trimIndent().replace("\n", lineSeparator())

    private var challenge = Day23()

    @BeforeEach
    fun setUp() {
        challenge.input = input
    }

    @Test
    fun testPart1() {
        val actual = challenge.part1()
        val expected = "12521"
        assertEquals(expected, actual)
    }

    @Test
    fun testPart2() {
        val actual = challenge.part2()
        val expected = "44169"
        assertEquals(expected, actual)
    }

    @Test
    fun testParseState() {
        val maze = Maze()
        val state = input.parseState(maze)
        assertEquals(8, state.locations.size)
    }

    @Test
    fun testParseMovement() {
        val maze = Maze()

        assertEquals(listOf(1.toByte()), maze.links(0.toByte()))
        assertEquals(listOf(9.toByte()), maze.links(10.toByte()))
        assertEquals(listOf(0.toByte(), 2.toByte()), maze.links(1.toByte()))
        assertEquals(listOf(2.toByte(), 4.toByte()), maze.links(3.toByte()))
        assertEquals(listOf(4.toByte(), 6.toByte()), maze.links(5.toByte()))
        assertEquals(listOf(6.toByte(), 8.toByte()), maze.links(7.toByte()))
        assertEquals(listOf(8.toByte(), 10.toByte()), maze.links(9.toByte()))

        assertEquals(listOf(11.toByte(), 1.toByte(), 3.toByte()), maze.links(2.toByte()))
        assertEquals(listOf(2.toByte(), 15.toByte()), maze.links(11.toByte()))
        assertEquals(listOf(11.toByte()), maze.links(15.toByte()))

        assertEquals(listOf(12.toByte(), 3.toByte(), 5.toByte()), maze.links(4.toByte()))
        assertEquals(listOf(4.toByte(), 16.toByte()), maze.links(12.toByte()))
        assertEquals(listOf(12.toByte()), maze.links(16.toByte()))

        assertEquals(listOf(13.toByte(), 5.toByte(), 7.toByte()), maze.links(6.toByte()))
        assertEquals(listOf(6.toByte(), 17.toByte()), maze.links(13.toByte()))
        assertEquals(listOf(13.toByte()), maze.links(17.toByte()))

        assertEquals(listOf(14.toByte(), 7.toByte(), 9.toByte()), maze.links(8.toByte()))
        assertEquals(listOf(8.toByte(), 18.toByte()), maze.links(14.toByte()))
        assertEquals(listOf(14.toByte()), maze.links(18.toByte()))
    }

    @Test
    fun testGoalPosition() {
        val maze = Maze()
        val state = State(listOf<Byte>(11,15,12,16,13,17,14,18), 0)

        val nextMoves = maze.nextMoves(state)
        assertEquals(0, nextMoves.size)
    }

    @Test
    fun testAllMovesAmphipod1() {
        val maze = Maze()
        val state = State(listOf<Byte>(12,15,11,16,13,17,14,18), 0)

        val nextMoves = maze.allMoves(state, 0)
        assertEquals(7, nextMoves.size)
    }


    @Test
    fun testMoveFromRoom() {
        val maze = Maze()
        val state = State(listOf<Byte>(12,15,11,16,13,17,14,18), 0)

        val nextMoves = maze.nextMoves(state)
        assertEquals(14, nextMoves.size)
    }

    @Test
    fun testMoveIntoRoom() {
        val maze = Maze()
        val state = State(listOf<Byte>(1, 15, 12, 16, 13, 17, 14, 18), 0)

        val nextMoves = maze.nextMoves(state)
        assertEquals(1, nextMoves.size)
    }


    @Test
    fun testMoveIntoRoom2() {
        val maze = Maze()
        val state = State(listOf<Byte>(5,15,1,16,13,17,14,18), 0)
        val nextMoves = maze.nextMoves(state)
        assertEquals(2, nextMoves.size)
    }

    @Test
    fun testMoveIntoRoom3() {
        val maze = Maze()
        val state = State(listOf<Byte>(5,15,12,16,13,17,14,18), 0)
        val nextMoves = maze.nextMoves(state)
        assertEquals(1, nextMoves.size)
    }

    @Test
    fun testBottomPosition2() {
        val maze = Maze()
        val state = State(listOf<Byte>(5,15,1,16,13,17,14,18), 0)
        val isBottom = maze.isBottomPosition(state,2, 12)
        assertTrue(isBottom)
    }
    @Test
    fun testSolveSimpleMove() {
        val maze = Maze(2)
        val state = State(listOf<Byte>(1,15,12,16,13,17,14,18), 0)
        val actual = maze.solve(state)
        assertEquals(2, actual)
    }

    @Test
    fun testSolveSimpleSwap() {
        val maze = Maze(2)
        val state = State(listOf<Byte>(12,15,11,16,13,17,14,18), 0)
        val actual = maze.solve(state)
        assertEquals(46, actual)
    }

    @Test
    fun testSolvePart1() {
        val maze = Maze(2)
        val state = input.parseState(maze)
        val actual = maze.solve(state)
        assertEquals(12521, actual)
    }

    @Test
    fun testSolvePart2() {
        val maze = Maze(4)
        val state = input.parseState(maze)
        val actual = maze.solve(state)
        assertEquals(44169, actual)
    }
}
