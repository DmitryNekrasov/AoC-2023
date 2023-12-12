import java.io.File

fun solvePartOne(spring: String, record: List<Int>): Int {
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

fun solve(initialSpring: String, initialRecord: List<Int>, times: Int): Long {
    val spring = List(times) { initialSpring }.joinToString("?") + "."
    val record = List(times) { initialRecord }.flatten()
    val cache = HashMap<Triple<Int, Int, Int>, Long>()
    fun calc(springIndex: Int, recordIndex: Int, hashLen: Int): Long {
        if (springIndex == spring.length && recordIndex == record.size) return 1L
        if (springIndex == spring.length) return 0L
        val triple = Triple(springIndex, recordIndex, hashLen)
        if (triple in cache) {
            return cache[triple]!!
        }
        if (spring[springIndex] == '#') return calc(springIndex + 1, recordIndex, hashLen + 1)
        if (spring[springIndex] == '.') {
            if (hashLen == 0) {
                return calc(springIndex + 1, recordIndex, 0)
            } else {
                if (recordIndex == record.size) return 0L
                return if (record[recordIndex] == hashLen) calc(springIndex + 1, recordIndex + 1, 0) else 0L
            }
        }
        // Put dot
        var result = 0L
        if (hashLen == 0) {
            result += calc(springIndex + 1, recordIndex, 0)
        } else {
            if (recordIndex == record.size) return 0L
            result += if (hashLen == record[recordIndex]) calc(springIndex + 1, recordIndex + 1, 0) else 0L
        }
        // Put hash
        result += calc(springIndex + 1, recordIndex, hashLen + 1)
        cache[triple] = result
        return result;
    }
    return calc(0, 0, 0)
}

fun main() {
    var ansPartOne = 0L
    var ansPartTwo = 0L
    for (line in File("input.txt").useLines { it.toList() }) {
        val (spring, recordString) = line.split(" ")
        val record = recordString.split(",").map { it.toInt() }
        ansPartOne += solve(spring, record, 1)
        ansPartTwo += solve(spring, record, 5)
    }
    println(ansPartOne)
    println(ansPartTwo)
}