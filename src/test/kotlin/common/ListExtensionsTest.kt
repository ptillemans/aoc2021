package common

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ListExtensionsTest {


    @Test
    fun testSplitOn() {
        val actual = listOf(1,2,3).splitOn { x -> x % 2 == 0 }
        val expected = listOf(listOf(1), listOf(3))
        Assertions.assertEquals(expected, actual)
    }

}