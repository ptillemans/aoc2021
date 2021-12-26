package aoc2021
import io.ktor.utils.io.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import java.lang.System.lineSeparator

class Day19Test {
    private val input = Day19Test::class.java.getResource("/aoc2021/day19/sample.txt").readText()
    val scanners = input.toScanners()

    private var challenge = Day19() 
    
    @BeforeEach
    fun setUp() {
        challenge.input = input
    }
   
    @Test
    fun testPart1() {
        val actual = challenge.part1()
        val expected = "79"
        assertEquals(expected, actual)
    }
   
    @Test
    fun testPart2() {
        val actual = challenge.part2()
        val expected = "3621"
        assertEquals(expected, actual)
    }

    @Test
    fun testParser() {
        assertEquals(5, scanners.size)
        assertEquals(Coordinate(0,0,0), scanners[0].location)
        assertEquals(Orientation(Rotation.Rot0, Rotation.Rot0, Rotation.Rot0), scanners[0].orientation)
    }

    @Test
    fun testIntersection() {
        val dist1 = scanners[0].toDistances()
        val dist2 = scanners[1].toDistances()
        val actual = dist1.intersection(dist2)

        // 12 beacons --> 11 + 10 + .. + 1 pairs
        assertEquals(11*(11+1)/2, actual.size)
    }

    @Test
    fun testOverlappingBeacons1() {
        val scanner1Beacons = """
            -618,-824,-621
            -537,-823,-458
            -447,-329,318
            404,-588,-901
            544,-627,-890
            528,-643,409
            -661,-816,-575
            390,-675,-793
            423,-701,434
            -345,-311,381
            459,-707,401
            -485,-357,347
            """.trimIndent().split("\n")
            .map { it.toCoordinate() }
        val scanner2Beacons = """
            686,422,578
            605,423,415
            515,917,-361
            -336,658,858
            -476,619,847
            -460,603,-452
            729,430,532
            -322,571,750
            -355,545,-477
            413,935,-424
            -391,539,-444
            553,889,-390
            """.trimIndent().split("\n")
            .map { it.toCoordinate() }

        val expected = scanner1Beacons.zip(scanner2Beacons.map {setOf(it)}).toMap()
        val dist1 = scanners[0].toDistances()
        val dist2 = scanners[1].toDistances()
        val actual = dist1.intersection(dist2).overlappingBeaconsInDistList()
        assertEquals(expected, actual)
    }

    @Test
    fun testAllowableRotations() {
        val delta1 = Coordinate(1, 2, 3)
        val delta2 = Coordinate(1, 2, 3)

        val actual = allowableOrientation(delta1, delta2)
        val expected = setOf(Orientation(Rotation.Rot0, Rotation.Rot0, Rotation.Rot0))
        assertEquals(expected, actual)
    }

    @Test
    fun testFindBestLocationAndRotation1() {
        val dist1 = scanners[0].toDistances()
        val dist2 = scanners[1].toDistances()
        val expected = Coordinate(68,-1246,-43)
        val actual = dist1
            .intersection(dist2)
            .overlappingBeaconsInDistList()
            .findBestLocationAndOrientation()
        assertEquals(expected, actual!!.first)
    }

    @Test
    fun testFindBestLocationAndRotation2() {
        scanners[1].location = Coordinate(68, -1246, -43)
        scanners[1].orientation = Orientation(Rotation.Rot0, Rotation.Rot180, Rotation.Rot0)
        val dist1 = scanners[1]
            .toDistances()
            .map { BeaconDistance(it.beacons.map { scanners[1].relToAbs(it) }.toSet(), it.distance)}
        val dist2 = scanners[4].toDistances()
        val expected = Coordinate(-20,-1133,1061)
        val overlappingBeacons = dist1
            .intersection(dist2)
            .overlappingBeaconsInDistList()
        val actual = overlappingBeacons
            .findBestLocationAndOrientation()

        assertEquals(expected, actual!!.first)

        val expectedBeacons = """
            459,-707,401
            -739,-1745,668
            -485,-357,347
            432,-2009,850
            528,-643,409
            423,-701,434
            -345,-311,381
            408,-1815,803
            534,-1912,768
            -687,-1600,576
            -447,-329,318
            -635,-1737,486 
            """.trimIndent()
            .split("\n")
            .map { it.toCoordinate() }
            .toSet()

        val scanner = scanners[4]
        scanner.location = actual.first
        scanner.orientation = actual.second
        val actualBeacons = overlappingBeacons.flatMap { (k, v) -> v.map{scanner.relToAbs(it)} }.toSet()

        assertEquals(expectedBeacons, actualBeacons)
    }

    @Test
    fun testFindBeacons() {
        val expected = Day19Test::class.java.getResource("/aoc2021/day19/beacons.txt")
            .readText()
            .split(lineSeparator())
            .map { it.toCoordinate() }
            .toSet()
        val actual = scanners.findAllBeacons()

        val difference = actual - expected

        assertEquals(expected, actual)
    }
}
