import java.io.File

fun solvePartOne(steps: List<String>): Int {
    println(steps)
    return -1
}

fun main() {
    val steps = File("input_simple.txt").useLines { it.toList() }.first().split(",")
    val ansPartOne = solvePartOne(steps)
    println(ansPartOne)
}