import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.File
import java.time.LocalDate
import java.time.Month
import khttp.get

plugins {
    kotlin("jvm") version "1.5.21"
    application
}

group = "me.pti"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

application {
    mainClassName = "MainKt"
}


buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.danilopianini:khttp:0.1.0-dev30+51fa9ae")
    }
}

abstract class AdventFetcher : DefaultTask() {

    fun readCookies() : Map<String, String> {
        return File("cookies.txt")
            .readLines()
            .asSequence()
            .filter { !it.startsWith("#") }
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .map { it.split("\t") }
            .filter { it.size == 7 }.associate { it[5] to it[6] }
    }

    fun getInput(year: Int, day: Int): Boolean {

        val url = "https://adventofcode.com/${year}/day/${day}/input"
        val r = get(url, cookies = readCookies())
        if (r.statusCode != 200) {
            return false
        }

        val pathname = "src/main/resources/aoc${year}/day${day}"
        val text = r.text
        return writeFile(pathname, "input.txt", text)
    }

    fun generateBoilerPlate(year: Int, day: Int, hasInput : Boolean) {

        val inputPart = """
                
                fun getInput(): String {
                    val filename = "/aoc${year}/day${day}/input.txt"
                    return Day${day}::class.java.getResource(filename).readText()
                } 
        """

        val text = """
            package aoc${year}
           
            class Day${day} {
                ${if (hasInput) inputPart else "\n"}
                fun part1(): Int? {
                    return null
                }
               
                fun part2(): Int?{
                    return null
                }
                
                fun main() {
                    println("Part1: ")
                    println(part1())
                    println("Part2: ")
                    println(part2())
                }
            }

            fun main(args: Array<String>) {
                val challenge = Day${day}()
                challenge.main()
            }
        """.trimIndent()

        val pathname = "src/main/kotlin/aoc${year}"
        val filename = "Day${day}.kt"
        writeFile(pathname, filename, text)

        val testText = """
            package aoc${year}
            import aoc${year}.Day${day}
            import org.junit.jupiter.api.*;
           
            class Day${day}Test {
                ${if (hasInput) "        val input = \"\"\"\n            \"\"\".trimIndent()" else "\n"}
                
                var challenge = Day${day}() 
                
                @BeforeEach
                fun setUp() {
                }
               
                @Test
                fun testPart1() {
                    
                }
               
                @Test
                fun testPart2() {
                    
                }
                
            }

        """.trimIndent()

        val testPathname = "src/test/kotlin/aoc${year}"
        val testFilename = "Day${day}Test.kt"
        writeFile(testPathname, testFilename, testText)
    }

    fun writeFile(pathname: String, filename: String, text: String ) : Boolean {
        val path = File(pathname)
        if (path.isDirectory || path.mkdirs() ) {
            val file = File(path, filename)
            if (!file.isFile || forceOverwrite) {
                println("Creating text file " + file.canonicalFile)
                file.writeText(text)
            } else {
                println("Files already exist and --force flag not present.")
            }
            return true
        }
        println("ERROR: Could not create text file...")
        return false
    }

    @set:Option(option="date", description = "Use a different date that today for older challenges.")
    @get:Input
    var overrideDate: String? = null

    @set:Option(option="force", description = "Overwrite existing files.")
    @get:Input
    var forceOverwrite: Boolean = false


    @TaskAction
    fun fetchAdvent() {
        var fetchDate = if (overrideDate == null) LocalDate.now() else LocalDate.parse(overrideDate)
        val day = fetchDate.dayOfMonth
        val year = fetchDate.year

        if (fetchDate.month == Month.DECEMBER && fetchDate.dayOfMonth <= 25) {
            val hasInput = getInput(year, day)
            generateBoilerPlate(year, day, hasInput)
        } else {
            println("Error : Not an advent day!!!")
        }
    }
}

tasks.register<Build_gradle.AdventFetcher>("fetch")


