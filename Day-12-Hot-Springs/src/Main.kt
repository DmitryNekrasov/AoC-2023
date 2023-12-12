import java.io.File

fun solve(spring: String, record: List<Int>): Int {
    fun backtrack(spring: CharArray, index: Int): Int {
        if (index == spring.size) {
            return if (String(spring).split(".").filter { it.isNotEmpty() }.map { it.length } == record) 1 else 0
        }
        if (spring[index] == '?') {
            spring[index] = '#'
            val r1 = backtrack(spring, index + 1)
            spring[index] = '.'
            val r2 = backtrack(spring, index + 1)
            spring[index] = '?'
            return r1 + r2
        } else {
            return backtrack(spring, index + 1)
        }
    }
    return backtrack(spring.toCharArray(), 0)
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