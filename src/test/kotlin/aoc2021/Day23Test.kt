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
    fun testHallway() {
        val maze = generateMaze()
        val hallway = maze.cells.filter { it.isHallway() }
        assertTrue(hallway.all  { it.north == null })
        assertEquals(11, hallway.size)
        assertTrue(hallway.zipWithNext().all {it.first == it.second.west})
        assertTrue(hallway.zipWithNext().all {it.first.east == it.second})
    }

    @Test
    fun testRoomPart1() {
        val maze = generateMaze()
        val rooms = maze.cells.filter { it.isRoom() }
        assertTrue(rooms.all  { it.north != null })

        assertEquals(8, rooms.size)
    }

    @Test
    fun testParseState() {
        val maze = generateMaze()
        val state = input.parseState(maze)
        assertEquals(8, state.locations.size)
    }

    @Test
    fun testSolvePart1() {
        val maze = generateMaze()
        val state = input.parseState(maze)
        val actual = state.solve()
        assertEquals(12521, actual)
    }

    @Test
    fun testSolvePart2() {
        val maze = generateMaze(4)
        val state = input.parseState(maze, true)
        val actual = state.solve()
        assertEquals(44169, actual)
    }
}
