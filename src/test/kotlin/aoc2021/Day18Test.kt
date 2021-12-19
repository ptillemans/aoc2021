package aoc2021
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import java.lang.System.lineSeparator

class Day18Test {
    private val input = Day18Test::class.java.getResource("/aoc2021/day18/sample.txt")!!.readText()

    private var challenge = Day18() 
    
    @BeforeEach
    fun setUp() {
        challenge.input = input
    }
   
    @Test
    fun testPart1() {
        val actual = challenge.part1()
        val expected = "4140"
        assertEquals(expected, actual)
    }
   
    @Test
    fun testPart2() {
        val actual = challenge.part2()
        val expected = "3993"
        assertEquals(expected, actual)
    }

    @Test
    fun testParser() {
        input.split(lineSeparator())
            .map {it.toSnailFishNumber()}
            .forEach { assertTrue(it.numbers.all { it.level >= 0}) }
    }

    @Test
    fun testParser2() {
        val actual = "[[9,8], 1]".toSnailFishNumber()
        val expected = SnailFishNumber(listOf(
            RegularNumber(9, 2),
            RegularNumber(8, 2),
            RegularNumber(1,1)
        ))
        assertEquals(expected, actual)
    }

    @Test
    fun testReduceExplode1() {
        val actual = "[[[[[9,8],1],2],3],4]".toSnailFishNumber().reduce()
        val expected = "[[[[0,9],2],3],4]".toSnailFishNumber()
        assertEquals(expected, actual)
    }

    @Test
    fun testReduceExplode2() {
        val actual = "[7,[6,[5,[4,[3,2]]]]]".toSnailFishNumber().reduce()
        val expected = "[7,[6,[5,[7,0]]]]".toSnailFishNumber()
        assertEquals(expected, actual)
    }


    @Test
    fun testReduceExplode3() {
        val actual = "[[6,[5,[4,[3,2]]]],1]".toSnailFishNumber().reduce()
        val expected = "[[6,[5,[7,0]]],3]".toSnailFishNumber()
        assertEquals(expected, actual)
    }

    @Test
    fun testReduceExplode4() {
        val actual = "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]".toSnailFishNumber().reduceOnce()
        val expected = "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]".toSnailFishNumber()
        assertEquals(expected, actual)
    }

    @Test
    fun testReduceExplode5() {
        val actual = "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]".toSnailFishNumber().reduce()
        val expected = "[[3,[2,[8,0]]],[9,[5,[7,0]]]]".toSnailFishNumber()
        assertEquals(expected, actual)
    }

    @Test
    fun testAdditionWithReduce() {
        val actual = "[[[[4,3],4],4],[7,[[8,4],9]]]".toSnailFishNumber() + "[1,1]".toSnailFishNumber()
        val expected = "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]".toSnailFishNumber()
        assertEquals(expected, actual)
    }

    @Test
    fun testAddition() {
        val a = SnailFishNumber(listOf(RegularNumber(1, 1), RegularNumber(2,1)))
        val b = SnailFishNumber(listOf(RegularNumber(3,2), RegularNumber(4, 2), RegularNumber(5, 1)))
        val expected = SnailFishNumber(listOf(RegularNumber(1,2), RegularNumber(2, 2), RegularNumber(3,3), RegularNumber(4,3), RegularNumber(5,2) ))
        assertEquals(expected, a + b)
    }

    @Test
    fun testSum() {
        val actual = listOf(
            "[1,1]",
                    "[2,2]",
                    "[3,3]",
                    "[4,4]",
        ).map {it.toSnailFishNumber()}.sum()
        val expected = "[[[[1,1],[2,2]],[3,3]],[4,4]]".toSnailFishNumber()
        assertEquals(expected, actual)
    }

    @Test
    fun testSum2() {
        val actual = listOf(
            "[1,1]",
            "[2,2]",
            "[3,3]",
            "[4,4]",
            "[5,5]",
        ).map {it.toSnailFishNumber()}.sum()
        val expected = "[[[[3,0],[5,3]],[4,4]],[5,5]]".toSnailFishNumber()
        assertEquals(expected, actual)
    }

    @Test
    fun testSum3() {
        val actual = listOf(
            "[1,1]",
            "[2,2]",
            "[3,3]",
            "[4,4]",
            "[5,5]",
            "[6,6]"
        ).map {it.toSnailFishNumber()}.sum()
        val expected = "[[[[5,0],[7,4]],[5,5]],[6,6]]".toSnailFishNumber()
        assertEquals(expected, actual)
    }

    @Test
    fun testSum4() {
        val actual = listOf(
            "[1,1]",
            "[2,2]",
            "[3,3]",
            "[4,4]",
            "[5,5]",
            "[6,6]"
        ).map {it.toSnailFishNumber()}.sum()
        val expected = "[[[[5,0],[7,4]],[5,5]],[6,6]]".toSnailFishNumber()
        assertEquals(expected, actual)
    }

    @Test
    fun testSum5() {
        val actual = listOf(
            "[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]",
                    "[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]",
                    "[[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]",
                    "[[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]",
                    "[7,[5,[[3,8],[1,4]]]]",
                    "[[2,[2,2]],[8,[8,1]]]",
                    "[2,9]",
                    "[1,[[[9,3],9],[[9,0],[0,7]]]]",
                    "[[[5,[7,4]],7],1]",
                     "[[[[4,2],2],6],[8,7]]",
        ).map {it.toSnailFishNumber()}.sum()
        val expected = "[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]".toSnailFishNumber()
        assertEquals(expected, actual)
    }

    @Test
    fun testMagnitude1() {
        assertEquals(29, "[9,1]".toSnailFishNumber().magnitude())
    }

    @Test
    fun testMagnitude2() {
        assertEquals(129, "[[9,1],[1,9]]".toSnailFishNumber().magnitude())
        assertEquals(143, "[[1,2],[[3,4],5]]".toSnailFishNumber().magnitude())
        assertEquals(1384, "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]".toSnailFishNumber().magnitude())
        assertEquals(445, "[[[[1,1],[2,2]],[3,3]],[4,4]]".toSnailFishNumber().magnitude())
        assertEquals(791, "[[[[3,0],[5,3]],[4,4]],[5,5]]".toSnailFishNumber().magnitude())
        assertEquals(1137, "[[[[5,0],[7,4]],[5,5]],[6,6]]".toSnailFishNumber().magnitude())
        assertEquals(3488, "[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]".toSnailFishNumber().magnitude())
    }

    @Test
    fun testHomework() {
        val numbers = listOf(
            "[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]",
                    "[[[5,[2,8]],4],[5,[[9,9],0]]]",
                    "[6,[[[6,2],[5,6]],[[7,6],[4,7]]]]",
                    "[[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]",
                    "[[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]",
                    "[[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]",
                    "[[[[5,4],[7,7]],8],[[8,3],8]]",
                    "[[9,3],[[9,9],[6,[4,9]]]]",
                    "[[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]",
                    "[[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]",
        ).map { it.toSnailFishNumber() }
        val actual = numbers.homework()
        val expected: Long = 4140
        assertEquals(expected, actual)
    }


    @Test
    fun testHighestMagnitude() {
        val numbers = listOf(
            "[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]",
            "[[[5,[2,8]],4],[5,[[9,9],0]]]",
            "[6,[[[6,2],[5,6]],[[7,6],[4,7]]]]",
            "[[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]",
            "[[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]",
            "[[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]",
            "[[[[5,4],[7,7]],8],[[8,3],8]]",
            "[[9,3],[[9,9],[6,[4,9]]]]",
            "[[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]",
            "[[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]",
        ).map { it.toSnailFishNumber() }
        val actual = numbers.highestMagnitudeSum()
        val expected: Long = 3993
        assertEquals(expected, actual)
    }

}
