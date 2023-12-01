import java.io.File

fun main() {
    val input = File("input.txt").useLines { it.toList() }
    input.sumOf { s -> s.first { it.isDigit() }.digitToInt() * 10 + s.last { it.isDigit() }.digitToInt() }
        .also { println(it) }

    val dict = hashMapOf("one" to 1, "two" to 2, "three" to 3, "four" to 4, "five" to 5,
                         "six" to 6, "seven" to 7, "eight" to 8, "nine" to 9)
    (0..9).forEach { dict[it.toString()] = it }
    input.sumOf { s ->
        var leftIndex = Int.MAX_VALUE
        var rightIndex = -1
        var leftValue = 0
        var rightValue = 0
        for ((key, value) in dict) {
            val leftIndexCandidate = s.indexOf(key)
            if (leftIndexCandidate != -1 && leftIndexCandidate < leftIndex) {
                leftIndex = leftIndexCandidate
                leftValue = value
            }
            val rightIndexCandidate = s.lastIndexOf(key)
            if (rightIndexCandidate > rightIndex) {
                rightIndex = rightIndexCandidate
                rightValue = value
            }
        }
        leftValue * 10 + rightValue
    }.also { println(it) }
}