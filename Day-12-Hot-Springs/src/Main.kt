import java.io.File

fun solve(spring: String, record: List<Int>): Int {
    println("$spring, $record")

    return -1
}

fun main() {
    var ansPartOne = 0
    for (line in File("input.txt").useLines { it.toList() }) {
        val (spring, recordString) = line.split(" ")
        val record = recordString.split(",").map { it.toInt() }
        ansPartOne += solve(spring, record)
    }
    println(ansPartOne)
}