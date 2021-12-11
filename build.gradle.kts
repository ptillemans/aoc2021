import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.File
import java.time.LocalDate
import java.time.Month
import khttp.get

plugins {
    kotlin("jvm") version "1.6.0"
    application
    id("org.jetbrains.kotlinx.kover") version "0.4.4"
}

group = "me.pti"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.google.code.gson:gson:2.8.9")
    implementation("org.danilopianini:khttp:0.1.0-dev30+51fa9ae")
}

tasks.test {
    useJUnitPlatform()
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

application {
    mainClass.set("MainKt")
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

    private fun readCookies() : Map<String, String> {
        return File("cookies.txt")
            .readLines()
            .asSequence()
            .filter { !it.startsWith("#") }
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .map { it.split("\t") }
            .filter { it.size == 7 }.associate { it[5] to it[6] }
    }

    private fun getInput(year: Int, day: Int): Boolean {

        val url = "https://adventofcode.com/${year}/day/${day}/input"
        val r = get(url, cookies = readCookies())
        if (r.statusCode != 200) {
            return false
        }

        val pathname = "src/main/resources/aoc${year}/day${day}"
        val text = r.text
        return writeFile(pathname, "input.txt", text)
    }

    private fun generateBoilerPlate(year: Int, day: Int, hasInput : Boolean) {

        val inputPart = """
                var input: String
                
                init {
                    val filename = "/aoc${year}/day${day}/input.txt"
                    input = Day${day}::class.java.getResource(filename).readText()
                }
                
        """

        val text = """
            package aoc${year}
            
            import com.google.gson.Gson 
            
            class Day${day} {
                ${if (hasInput) inputPart else "\n"}
                fun part1(): String? {
                    return null
                }
               
                fun part2(): String? {
                    return null
                }
                
            }

            fun main() {
                val challenge = Day${day}()
                val solutions = mapOf (
                    "part1" to challenge.part1(),
                    "part2" to challenge.part2(),
                )
                println("Solutions: ")
                println(Gson().toJson(solutions))
            }
        """.trimIndent()

        val pathname = "src/main/kotlin/aoc${year}"
        val filename = "Day${day}.kt"
        writeFile(pathname, filename, text)

        val testText = """
            package aoc${year}
            import org.junit.jupiter.api.*
            import org.junit.jupiter.api.Assertions.*
           
            class Day${day}Test {
                ${if (hasInput) "private val input = \"\"\"\n            \"\"\".trimIndent()" else "\n"}
                
                private var challenge = Day${day}() 
                
                @BeforeEach
                fun setUp() {
                    ${if (hasInput) "challenge.input = input" else ""}
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
                
            }

        """.trimIndent()

        val testPathname = "src/test/kotlin/aoc${year}"
        val testFilename = "Day${day}Test.kt"
        writeFile(testPathname, testFilename, testText)
    }

    private fun writeFile(pathname: String, filename: String, text: String ) : Boolean {
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
    var overrideDate: String= ""

    @set:Option(option="force", description = "Overwrite existing files.")
    @get:Input
    var forceOverwrite: Boolean = false


    @TaskAction
    fun fetchAdvent() {
        val fetchDate = if (overrideDate == "") LocalDate.now() else LocalDate.parse(overrideDate)
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

tasks.register<AdventFetcher>("fetch")

task<JavaExec>("runChallenge") {
    mainClass.set("aoc2021.Day1Kt")
    classpath = project.sourceSets["main"].runtimeClasspath
}
