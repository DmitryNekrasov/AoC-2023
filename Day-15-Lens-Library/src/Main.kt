import java.io.File

const val BASE = 17
const val MOD = 256

fun String.hash() = this.fold(0) { acc, c -> (acc + c.code) * BASE % MOD }

fun solvePartOne(steps: List<String>) = steps.sumOf { it.hash() }

fun main() {
    val steps = File("input.txt").useLines { it.toList() }.first().split(",")
    val ansPartOne = solvePartOne(steps)
    println(ansPartOne)
}